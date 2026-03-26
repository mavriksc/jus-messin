import argparse
import json
import os
from typing import Any, Dict, List, Tuple

from PIL import Image, ImageColor, ImageDraw, ImageFont


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Render exact text onto an image from a JSON spec.")
    parser.add_argument("--spec", required=True, help="Path to JSON spec file.")
    parser.add_argument("--out", required=True, help="Output image path (PNG recommended).")
    return parser.parse_args()


def load_spec(path: str) -> Dict[str, Any]:
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)


def parse_color(value: str) -> Tuple[int, int, int, int]:
    return ImageColor.getcolor(value, "RGBA")


def load_font(font_path: str, size: int) -> ImageFont.FreeTypeFont:
    return ImageFont.truetype(font_path, size=size)


def find_default_font_path() -> str:
    candidates = [
        "C:/Windows/Fonts/arial.ttf",
        "C:/Windows/Fonts/ARIAL.TTF",
    ]
    for path in candidates:
        if os.path.exists(path):
            return path
    return ""


def wrap_text(draw: ImageDraw.ImageDraw, text: str, font: ImageFont.FreeTypeFont, max_width: int) -> List[str]:
    words = text.split()
    if not words:
        return [""]
    lines: List[str] = []
    current = words[0]
    for word in words[1:]:
        test = f"{current} {word}"
        if draw.textlength(test, font=font) <= max_width:
            current = test
        else:
            lines.append(current)
            current = word
    lines.append(current)
    return lines


def measure_text_block(draw: ImageDraw.ImageDraw, lines: List[str], font: ImageFont.FreeTypeFont, line_spacing: int) -> Tuple[int, int]:
    heights = []
    max_width = 0
    for line in lines:
        bbox = draw.textbbox((0, 0), line, font=font)
        width = bbox[2] - bbox[0]
        height = bbox[3] - bbox[1]
        heights.append(height)
        max_width = max(max_width, width)
    total_height = sum(heights) + max(0, len(lines) - 1) * line_spacing
    return max_width, total_height


def get_font_options(item: Dict[str, Any], default_font: Dict[str, Any]) -> Dict[str, Any]:
    options = dict(default_font)
    options.update(item.get("font", {}))
    return options


def resolve_font(font_opts: Dict[str, Any]) -> ImageFont.FreeTypeFont:
    size = int(font_opts.get("size", 24))
    font_path = font_opts.get("path")
    if font_path:
        return load_font(font_path, size)
    default_path = find_default_font_path()
    if default_path:
        return load_font(default_path, size)
    return ImageFont.load_default()


def align_x(x: int, box_w: int, text_w: int, align: str) -> int:
    if align == "center":
        return x + (box_w - text_w) // 2
    if align == "right":
        return x + box_w - text_w
    return x


def align_y(y: int, box_h: int, text_h: int, valign: str) -> int:
    if valign == "middle":
        return y + (box_h - text_h) // 2
    if valign == "bottom":
        return y + box_h - text_h
    return y


def render(spec: Dict[str, Any], out_path: str) -> None:
    width = int(spec["width"])
    height = int(spec["height"])

    background = spec.get("background", {})
    if "image" in background:
        bg_img = Image.open(background["image"]).convert("RGBA")
        canvas = Image.new("RGBA", (width, height))
        bg_img = bg_img.resize((width, height), Image.LANCZOS)
        canvas.paste(bg_img, (0, 0))
    else:
        color = background.get("color", "#ffffff")
        canvas = Image.new("RGBA", (width, height), parse_color(color))

    draw = ImageDraw.Draw(canvas)
    default_font = spec.get("defaultFont", {})

    for item in spec["items"]:
        text = item["text"]
        x, y, w, h = item["box"]
        opts = get_font_options(item, default_font)
        font = resolve_font(opts)
        color = parse_color(opts.get("color", "#000000"))
        align = opts.get("align", "left")
        valign = opts.get("valign", "top")
        wrap = bool(opts.get("wrap", True))
        line_spacing = int(opts.get("lineSpacing", 4))

        if wrap:
            lines = wrap_text(draw, text, font, w)
        else:
            lines = text.split("\n")

        text_w, text_h = measure_text_block(draw, lines, font, line_spacing)
        cursor_x = align_x(x, w, text_w, align)
        cursor_y = align_y(y, h, text_h, valign)

        for idx, line in enumerate(lines):
            draw.text((cursor_x, cursor_y), line, font=font, fill=color)
            line_height = draw.textbbox((0, 0), line, font=font)[3]
            cursor_y += line_height + line_spacing

    canvas.save(out_path)


def main() -> None:
    args = parse_args()
    spec = load_spec(args.spec)
    render(spec, args.out)


if __name__ == "__main__":
    main()
