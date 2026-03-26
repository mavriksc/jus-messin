---
name: exact-text-image
description: Render exact-text images from a JSON spec with background (color or image), font selection, and explicit bounding boxes. Use when a user needs exact, non-diffusion text in images such as menus, cheatsheets, printables, or labels with precise positioning.
---

# Exact Text Image

## Quick start

Render an image from a JSON spec.

```
pip install pillow

python scripts/render_text_image.py --spec spec.json --out out.png
```

## JSON spec

Use a single JSON object. Required fields are `width`, `height`, `background`, and `items`. See [spec.md](D:/code/jus-messin/skills/exact-text-image/references/spec.md) for details.

```
{
  "width": 1600,
  "height": 900,
  "background": {
    "color": "#0e0f13"
  },
  "items": [
    {
      "text": "Neovim Cheatsheet",
      "box": [80, 60, 1440, 120],
      "font": {
        "path": "C:/Windows/Fonts/arial.ttf",
        "size": 52,
        "color": "#f2f2f2",
        "align": "left",
        "valign": "middle",
        "wrap": true
      }
    },
    {
      "text": "SPC f f   Find file",
      "box": [80, 200, 700, 60],
      "font": { "size": 26, "color": "#e6e6e6" }
    }
  ]
}
```

### Background

Use either a solid color or an image file.

```
"background": { "color": "#ffffff" }
```

```
"background": { "image": "C:/path/to/bg.png" }
```

### Items

Provide for each item:

- `text`: String (required)
- `box`: `[x, y, w, h]` (required)
- `font`: Optional overrides. If omitted, defaults apply.

Use these font options:

- `path`: TTF/OTF path (optional, default uses a bundled font if provided or system fallback)
- `size`: Number (required if no default)
- `color`: Hex color string
- `align`: `left`, `center`, `right`
- `valign`: `top`, `middle`, `bottom`
- `wrap`: Boolean; when true, wraps text inside the bounding box
- `lineSpacing`: Number; extra pixels between lines (optional)

## Defaults

If a text item omits font fields, apply defaults in this order:

1. Per-item `font` overrides
2. Root-level `defaultFont`
3. Script defaults (safe, readable)

## Workflow

1. Generate a layout plan: decide image size, sections, and boxes.
2. Emit JSON spec with explicit boxes for all text.
3. Run the script to produce a PNG.
4. If any text overflows, adjust boxes or font sizes and rerun.

## Notes

- Always prefer exact text rendering using this skill for printables, menus, and cheatsheets.
- If the user provides a background image, keep aspect ratio; fit it to canvas size.
- For printable output, target 300 DPI when sizing pixels (e.g., 2550x3300 for 8.5x11).
