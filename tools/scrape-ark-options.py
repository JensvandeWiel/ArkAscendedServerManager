#!/usr/bin/env python3
"""Regenerate ArkServerOptions.csv from the ARK Community Wiki.

The wiki page is built from ``{{Server config variable}}`` templates, so it can be
parsed reliably instead of hand-maintaining the CSV.

Usage:
    python tools/scrape-ark-options.py                 # update ArkServerOptions.csv
    python tools/scrape-ark-options.py --check         # report drift, exit 1 if any
    python tools/scrape-ark-options.py --input page.raw  # parse a saved wikitext file
    python tools/scrape-ark-options.py --output other.csv

The ``implemented`` column is preserved from the existing CSV (matched on
Location+name, then on name when unique), so a scrape never resets progress.

Requirements: Python 3.9+, no third-party packages.
"""

from __future__ import annotations

import argparse
import csv
import re
import sys
import urllib.request
from collections import Counter
from pathlib import Path

WIKI_URL = "https://ark.wiki.gg/wiki/Server_configuration?action=raw"
USER_AGENT = (
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/126.0 Safari/537.36"
)
TEMPLATE_START = "{{Server config variable"
COLUMNS = [
    "Location",
    "name",
    "inASA",
    "inASE",
    "default",
    "cli",
    "info",
    "version",
    "status",
    "type",
    "implemented",
]

REPO_ROOT = Path(__file__).resolve().parents[1]
DEFAULT_OUTPUT = REPO_ROOT / "ArkServerOptions.csv"


def fetch(url: str) -> str:
    request = urllib.request.Request(
        url,
        headers={
            "User-Agent": USER_AGENT,
            "Accept": "text/x-wiki,text/plain,*/*",
        },
    )
    with urllib.request.urlopen(request, timeout=60) as response:
        return response.read().decode("utf-8")


def find_template_blocks(text: str):
    """Yield (start, end) for each template invocation, brace-aware.

    Values may contain nested templates (``{{ItemLink|...}}``) and some templates
    close inline (``...}}`` on the same line), so simple line scanning is wrong.
    """
    pos = 0
    while True:
        start = text.find(TEMPLATE_START, pos)
        if start < 0:
            return
        depth = 0
        i = start
        while i < len(text):
            if text.startswith("{{", i):
                depth += 1
                i += 2
            elif text.startswith("}}", i):
                depth -= 1
                i += 2
                if depth == 0:
                    break
            else:
                i += 1
        yield start, i
        pos = i


def headings_before(text: str, offset: int):
    stack = []
    for match in re.finditer(r"^(={2,4})\s*(.*?)\s*\1\s*$", text[:offset], flags=re.M):
        level = len(match.group(1))
        title = match.group(2).strip()
        stack = [item for item in stack if item[0] < level]
        stack.append((level, title))
    return stack


def derive_location(headings) -> str:
    """Map the wiki heading path to the CSV Location convention.

    Game.ini has grouping headings (``Creature Spawn related`` etc.). Those are
    intentionally collapsed: the CSV Location must say which *file* an option
    belongs to, not how the wiki happens to group it.
    """
    file_name = None
    bracketed = None
    for _, title in headings:
        if title in ("GameUserSettings.ini", "Game.ini"):
            file_name = title
        if re.fullmatch(r"\[.+\]", title):
            bracketed = title

    if file_name:
        suffix = f" > {bracketed}" if bracketed else ""
        return f"Configuration Files > {file_name}{suffix}"

    # Top-level sections: "Command line options", "DynamicConfig", ...
    return headings[0][1] if headings else "Unknown"


def parse_fields(block: str) -> dict[str, str]:
    """Parse a template body into fields, keeping multi-line values intact."""
    # Drop the template name line and the final closing braces.
    body = block.split("\n", 1)[1] if "\n" in block else ""
    if body.endswith("}}"):
        body = body[:-2]

    fields: dict[str, str] = {}
    positional: list[str] = []
    current_key: str | None = None

    for line in body.split("\n"):
        if re.match(r"^\s*\|", line):
            content = re.sub(r"^\s*\|\s?", "", line)
            key_match = re.match(r"^([A-Za-z0-9_]+)\s*=\s*(.*)$", content)
            if key_match:
                current_key = key_match.group(1)
                fields[current_key] = key_match.group(2).strip()
            else:
                current_key = None
                if content.strip():
                    positional.append(content.strip())
        elif current_key is not None:
            # Preserve the wiki's indentation in multi-line values, but use CRLF
            # so the committed CSV stays stable and diffs stay small.
            fields[current_key] = f"{fields[current_key]}\r\n{line.rstrip()}"
        elif line.strip():
            positional.append(line.rstrip())

    fields["_positional"] = "\r\n".join(positional).strip()
    return fields


def normalize_presence(value: str, warnings: list[str], name: str) -> str:
    """Normalise the inASA/inASE columns; the wiki occasionally has typos."""
    raw = (value or "").strip()
    lowered = raw.lower()
    mapping = {
        "yes": "Yes",
        "true": "Yes",
        "1": "Yes",
        # Known wiki typo: "Tes" on UseAstraeosTraversalBuff.
        "tes": "Yes",
        "no": "No",
        "false": "No",
        "0": "No",
        "unknown": "Unknown",
        "unk": "Unknown",
        "?": "Unknown",
    }
    if lowered in mapping:
        canonical_lower = {"yes", "no", "unknown", "true", "false", "1", "0", "?"}
        if lowered not in canonical_lower:
            warnings.append(f"normalised inASA/inASE value {raw!r} -> {mapping[lowered]!r} for {name}")
        return mapping[lowered]
    if raw:
        warnings.append(f"unrecognised inASA/inASE value {raw!r} for {name}")
    return raw


def parse_rows(text: str) -> tuple[list[dict[str, str]], list[str]]:
    rows: list[dict[str, str]] = []
    warnings: list[str] = []

    for start, end in find_template_blocks(text):
        block = text[start:end]
        fields = parse_fields(block)
        name = fields.get("name", "").strip()
        if not name:
            warnings.append(f"template without a name at offset {start}")
            continue

        info = fields.get("info", "").strip() or fields.get("_positional", "").strip()
        rows.append(
            {
                "Location": derive_location(headings_before(text, start)),
                "name": name,
                "inASA": normalize_presence(fields.get("inASA", ""), warnings, name),
                "inASE": normalize_presence(fields.get("inASE", ""), warnings, name),
                "default": fields.get("default", "").strip(),
                "cli": fields.get("cli", "").strip(),
                "info": info,
                "version": fields.get("version", "").strip(),
                "status": fields.get("status", "").strip(),
                "type": fields.get("type", "").strip(),
                "implemented": "",
            }
        )

    return rows, warnings


def read_existing(path: Path) -> list[dict[str, str]]:
    if not path.exists():
        return []
    with path.open(encoding="utf-8-sig", newline="") as handle:
        return list(csv.DictReader(handle))


def carry_over_implemented(new_rows, old_rows):
    """Keep previously ticked options ticked across a scrape."""
    by_key = {(row.get("Location"), row.get("name")): row.get("implemented", "") for row in old_rows}
    name_counts = Counter(row.get("name") for row in old_rows)
    by_name = {
        row.get("name"): row.get("implemented", "")
        for row in old_rows
        if name_counts[row.get("name")] == 1
    }

    for row in new_rows:
        value = by_key.get((row["Location"], row["name"]), "")
        if not value:
            value = by_name.get(row["name"], "")
        row["implemented"] = value or ""


def normalized(value: str) -> str:
    """Compare values ignoring line endings, indentation and repeated spaces."""
    return re.sub(r"\s+", " ", value or "").strip()


def preview(value: str, limit: int = 160) -> str:
    text = re.sub(r"\s+", " ", value or "").strip()
    return text if len(text) <= limit else text[: limit - 1] + "…"


def diff_rows(old_rows, new_rows, limit: int = 100):
    old_map = {(row.get("Location"), row.get("name")): row for row in old_rows}
    new_map = {(row.get("Location"), row.get("name")): row for row in new_rows}

    added = sorted(set(new_map) - set(old_map))
    removed = sorted(set(old_map) - set(new_map))

    changed = []
    for key in sorted(set(old_map) & set(new_map)):
        old, new = old_map[key], new_map[key]
        fields = [(column, old.get(column, ""), new.get(column, "")) for column in COLUMNS if column != "implemented"]
        fields = [item for item in fields if normalized(item[1]) != normalized(item[2])]
        if fields:
            changed.append((key, fields))

    def show(items):
        for item in items[:limit]:
            yield item
        if len(items) > limit:
            yield f"... {len(items) - limit} more"

    return added, removed, changed, show


def write_csv(path: Path, rows: list[dict[str, str]]) -> None:
    """Write via a temp file so a failed/aborted run cannot truncate the CSV."""
    temporary = path.with_name(path.name + ".tmp")
    with temporary.open("w", encoding="utf-8", newline="") as handle:
        writer = csv.DictWriter(
            handle,
            fieldnames=COLUMNS,
            quoting=csv.QUOTE_MINIMAL,
            lineterminator="\r\n",
        )
        writer.writeheader()
        writer.writerows(rows)
    temporary.replace(path)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--input", type=Path, help="parse a saved wikitext file instead of fetching")
    parser.add_argument("--output", type=Path, default=DEFAULT_OUTPUT, help="CSV to write")
    parser.add_argument("--check", action="store_true", help="report drift without writing")
    parser.add_argument("--limit", type=int, default=100, help="max diff entries to print")
    args = parser.parse_args()

    text = args.input.read_text(encoding="utf-8") if args.input else fetch(WIKI_URL)
    rows, warnings = parse_rows(text)

    for warning in warnings:
        print(f"warning: {warning}", file=sys.stderr)

    old_rows = read_existing(args.output)
    carry_over_implemented(rows, old_rows)

    print(f"parsed {len(rows)} options from the wiki")
    for location, count in Counter(row["Location"] for row in rows).most_common():
        print(f"  {count:4d}  {location}")
    print("  inASA:", dict(Counter(row["inASA"] for row in rows)))

    if not old_rows:
        if args.check:
            print(f"{args.output} does not exist; would create it", file=sys.stderr)
            return 1
        write_csv(args.output, rows)
        print(f"wrote {args.output}")
        return 0

    added, removed, changed, show = diff_rows(old_rows, rows, args.limit)

    if not added and not removed and not changed:
        print("no changes")
        return 0

    print(f"\ndrift: {len(added)} added, {len(removed)} removed, {len(changed)} changed")

    if added:
        print("\nadded:")
        for key in show(added):
            print("  +", key if isinstance(key, str) else f"{key[0]} | {key[1]}")
    if removed:
        print("\nremoved:")
        for key in show(removed):
            print("  -", key if isinstance(key, str) else f"{key[0]} | {key[1]}")
    if changed:
        print("\nchanged:")
        for key, fields in changed[: args.limit]:
            print(f"  ~ {key[1]} ({key[0]})")
            for column, old, new in fields:
                print(f"      {column}: {preview(old)!r} -> {preview(new)!r}")
        if len(changed) > args.limit:
            print(f"  ... {len(changed) - args.limit} more")

    if args.check:
        return 1

    write_csv(args.output, rows)
    print(f"\nwrote {args.output}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
