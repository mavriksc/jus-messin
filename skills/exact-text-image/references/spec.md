# Spec Reference

## Required fields

- `width`: Canvas width in pixels (int)
- `height`: Canvas height in pixels (int)
- `background`: Object with `color` or `image`
- `items`: Array of text items

## Background

`background.color` uses CSS-style hex strings (e.g., `#ffffff`).

`background.image` is a path to an image file. It is resized to canvas size.

## Item fields

Each item must include:

- `text`: String
- `box`: `[x, y, w, h]` in pixels

Optional:

- `font`: object with:
  - `path`: TTF/OTF file path
  - `size`: font size in px
  - `color`: hex color
  - `align`: `left|center|right`
  - `valign`: `top|middle|bottom`
  - `wrap`: boolean
  - `lineSpacing`: extra px between lines

## Defaults

- If `font` omits fields, defaults come from root `defaultFont`.
- If `defaultFont.path` is missing, the script attempts `C:/Windows/Fonts/arial.ttf`, then falls back to Pillow default.
