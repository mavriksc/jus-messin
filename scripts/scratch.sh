#!/usr/bin/env zsh
set -e
set -u
set -o pipefail

SRC_ROOT="/media/media/movies"
OUT_ROOT="/media/media/mp4s"

# Create output directory if it doesn't exist
mkdir -p "$OUT_ROOT"

# Iterate immediate subdirectories of SRC_ROOT safely (handles spaces/newlines)
while IFS= read -r -d $'\0' dir; do
  # Find the first file over 8GB in the directory (no recursion beyond this level)
  big_file="$(find "$dir" -maxdepth 1 -type f -size +8G -print -quit || true)"

  # Skip if no qualifying file found
  if [[ -z "${big_file}" ]]; then
    continue
  fi

  # Derive folder name and output path
  foldername="$(basename "$dir")"
  out_file="$OUT_ROOT/${foldername}.mp4"

  # Skip if output already exists
  if [[ -e "$out_file" ]]; then
    echo "Skipping '$foldername' -> output already exists: $out_file"
    continue
  fi

  echo "Converting: '$big_file' -> '$out_file'"
  # Run the conversion
  ffmpeg -nostdin -i "$big_file" -c:v mpeg2video -q:v 2 -c:a mp2 -s 720x480 "$out_file"
done < <(find "$SRC_ROOT" -mindepth 1 -maxdepth 1 -type d -print0)