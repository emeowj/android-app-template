#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<EOF
Usage: $(basename "$0") [--filled] <icon-name>

Downloads the Material Symbols rounded Android vector drawable (XML) for the
supplied icon name and stores it in app/src/main/res/drawable/ic_<name>.xml.

Flags:
  --filled     Download the filled variant (writes ic_<name>_filled.xml).

Examples:
  $(basename "$0") repeat
  $(basename "$0") --filled home
EOF
}

icon_input=""
variant_segment="default"
output_suffix=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --filled)
      variant_segment="fill1"
      output_suffix="_filled"
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    -*)
      echo "Unknown option: $1" >&2
      usage
      exit 1
      ;;
    *)
      if [[ -n "$icon_input" ]]; then
        echo "Multiple icon names provided." >&2
        usage
        exit 1
      fi
      icon_input="$1"
      shift
      ;;
  esac
done

if [[ -z "$icon_input" ]]; then
  usage
  exit 1
fi

icon_slug=$(printf '%s' "$icon_input" | tr '[:upper:]' '[:lower:]' | tr ' ' '_' | tr '-' '_')

if [[ -z "$icon_slug" ]]; then
  echo "Icon name cannot be empty." >&2
  exit 1
fi

output_dir="app/src/main/res/drawable"
mkdir -p "$output_dir"

size_dp=24
style_segment="materialsymbolsrounded"
url="https://fonts.gstatic.com/s/i/short-term/release/${style_segment}/${icon_slug}/${variant_segment}/${size_dp}px.xml"

output_file="${output_dir}/ic_${icon_slug}${output_suffix}.xml"
variant_label="rounded"
if [[ "$variant_segment" == "fill1" ]]; then
  variant_label="rounded (filled)"
fi

echo "Downloading ${icon_slug} (${variant_label}, ${size_dp}dp) -> ${output_file}"
curl --fail --show-error --location "$url" --output "$output_file"

# Strip tint attribute referencing undefined theme colors.
python3 - "$output_file" <<'PY'
import sys
from pathlib import Path

path = Path(sys.argv[1])
lines = path.read_text().splitlines()
output_lines = []

for line in lines:
    stripped = line.strip()
    if 'android:tint=' in stripped:
        if stripped.endswith('>') and output_lines:
            prev = output_lines[-1].rstrip()
            if not prev.endswith('>'):
                prev += '>'
            output_lines[-1] = prev
        continue
    output_lines.append(line)

path.write_text("\n".join(output_lines) + "\n")
PY

echo "Done."
