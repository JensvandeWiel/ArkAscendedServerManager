#!/usr/bin/env python3
"""Build the ASA class-name catalogs used by the table editors.

The old ARK Server Manager shipped ``.gamedata`` JSON files with creature, item
and engram class names.  Those are ASE-era, so this script merges them with the
current ASA wiki lists and marks every entry as:

* ``yes``     - listed on an ASA-specific wiki page
* ``unknown`` - only found in the legacy ASE data

The generated files live in ``core/src/main/resources/catalogs/asa`` and are
committed so the application (and CI) never needs network access at runtime.

Run from the repository root:

    python tools/scrape-ark-catalogs.py
"""

from __future__ import annotations

import argparse
import datetime as dt
import json
import re
import sys
from pathlib import Path
from typing import Any, Iterable

try:  # Optional so the unit tests run on a bare Python install.
    import requests
    from bs4 import BeautifulSoup
except ImportError:  # pragma: no cover - only hit when the scraper is run without deps
    requests = None
    BeautifulSoup = None

WIKI = "https://ark.wiki.gg/wiki/"
CREATURE_URL = WIKI + "Creature_IDs"
ENGRAM_URL = WIKI + "Engram_class_names"
ITEM_SECTIONS = [
    "Resources",
    "Tools",
    "Armor",
    "Saddles",
    "Structures",
    "Vehicles",
    "Dye",
    "Consumables",
    "Recipes",
    "Eggs",
    "Farming",
    "Seeds",
    "Weapons",
    "Ammunition",
    "Skins",
    "Chibi_Pets",
    "Artifacts",
    "Trophy",
    "Unobtainable",
]
ITEM_URL = WIKI + "Item_IDs/{}"
DEFAULT_LEGACY = Path("../ServerManagers/src/ARKServerManager/GameData")
DEFAULT_OUT = Path("core/src/main/resources/catalogs/asa")


def fetch(url: str) -> BeautifulSoup:
    if requests is None or BeautifulSoup is None:
        raise RuntimeError(
            "scrape-ark-catalogs.py needs requests and beautifulsoup4; "
            "install them or use --skip-wiki"
        )
    response = requests.get(
        url,
        timeout=45,
        headers={"User-Agent": "ArkAscendedServerManager catalog builder"},
    )
    response.raise_for_status()
    return BeautifulSoup(response.text, "html.parser")


def cell_text(cell: Any) -> str:
    return " ".join(cell.get_text(" ", strip=True).split())


def display_name(cell: Any) -> str:
    links = cell.find_all("a")
    for link in reversed(links):
        text = " ".join(link.get_text(" ", strip=True).split())
        if text and not text.lower().endswith((".png", ".jpg", ".svg")):
            return text
    return cell_text(cell)


def class_from_blueprint(blueprint: str) -> str:
    match = re.search(r"\.([A-Za-z0-9_]+)'?\s*$", blueprint)
    if not match:
        match = re.search(r"\.([A-Za-z0-9_]+)'", blueprint)
    return f"{match.group(1)}_C" if match else ""


def mod_from_blueprint(blueprint: str) -> str:
    match = re.search(r"/Game/Mods/([^/]+)/", blueprint)
    return match.group(1) if match else "ArkPrime"


def iter_tables(soup: BeautifulSoup, required: str) -> Iterable[list[Any]]:
    for table in soup.find_all("table"):
        rows = table.find_all("tr")
        if not rows:
            continue
        header = [cell_text(cell) for cell in rows[0].find_all(["th", "td"])]
        if required not in header:
            continue
        for row in rows[1:]:
            cells = row.find_all(["td", "th"])
            if cells:
                yield cells


def parse_creatures() -> list[dict[str, Any]]:
    entries: dict[str, dict[str, Any]] = {}
    for cells in iter_tables(fetch(CREATURE_URL), "Entity ID"):
        if len(cells) < 4:
            continue
        name = display_name(cells[0])
        category = cell_text(cells[1])
        name_tag = cell_text(cells[2])
        class_name = cell_text(cells[3])
        blueprint = cell_text(cells[4]) if len(cells) > 4 else ""
        if not class_name:
            class_name = class_from_blueprint(blueprint)
        if not class_name or class_name in {"-", "—"}:
            continue
        entries[class_name] = {
            "name": name or class_name,
            "className": class_name,
            "category": category or None,
            "nameTag": name_tag or None,
            "mod": mod_from_blueprint(blueprint),
            "asa": "yes",
            "aliases": sorted({name, name_tag} - {"", None}),
        }
    return list(entries.values())


def parse_engrams() -> list[dict[str, Any]]:
    entries: dict[str, dict[str, Any]] = {}
    for cells in iter_tables(fetch(ENGRAM_URL), "Engram Class Name"):
        if len(cells) < 2:
            continue
        name = display_name(cells[0])
        class_name = cell_text(cells[1])
        if not class_name or class_name in {"-", "—"}:
            continue
        entries[class_name] = {
            "name": name or class_name,
            "className": class_name,
            "category": "Engrams",
            "nameTag": None,
            "mod": "ArkPrime",
            "asa": "yes",
            "aliases": [name] if name else [],
        }
    return list(entries.values())


def parse_items() -> list[dict[str, Any]]:
    entries: dict[str, dict[str, Any]] = {}
    for section in ITEM_SECTIONS:
        try:
            soup = fetch(ITEM_URL.format(section))
        except requests.RequestException as exc:  # pragma: no cover - network only
            print(f"warning: skipping item section {section}: {exc}", file=sys.stderr)
            continue
        for cells in iter_tables(soup, "Class Name"):
            if len(cells) < 6:
                continue
            name = display_name(cells[0])
            category = cell_text(cells[1]) or section
            class_name = cell_text(cells[4])
            blueprint = cell_text(cells[5])
            if not class_name:
                class_name = class_from_blueprint(blueprint)
            if not class_name or class_name in {"-", "—"}:
                continue
            if class_name.startswith("EngramEntry_"):
                continue
            entries[class_name] = {
                "name": name or class_name,
                "className": class_name,
                "category": category,
                "nameTag": None,
                "mod": mod_from_blueprint(blueprint),
                # The item-ID page is not ASA-specific.  Keep the entry but let
                # the UI show it as unverified in ASA.
                "asa": "unknown",
                "aliases": [name] if name else [],
            }
    return list(entries.values())


def load_legacy(legacy_dir: Path) -> dict[str, list[dict[str, Any]]]:
    result: dict[str, list[dict[str, Any]]] = {"creatures": [], "engrams": [], "items": []}
    if not legacy_dir.exists():
        print(f"warning: legacy game data not found at {legacy_dir}", file=sys.stderr)
        return result
    for path in sorted(legacy_dir.glob("*.gamedata")):
        if path.stem in {"PrimitivePlus", "PGM"}:
            continue
        with path.open(encoding="utf-8-sig") as handle:
            data = json.load(handle)
        for creature in data.get("Creatures", []):
            result["creatures"].append(creature)
        for engram in data.get("Engrams", []):
            result["engrams"].append(engram)
        for item in data.get("Items", []):
            result["items"].append(item)
    return result


def merge(
    wiki_entries: list[dict[str, Any]],
    legacy_entries: list[dict[str, Any]],
    kind: str,
) -> list[dict[str, Any]]:
    merged: dict[str, dict[str, Any]] = {}

    for legacy in legacy_entries:
        class_name = (legacy.get("ClassName") or "").strip()
        if not class_name or legacy.get("Mod") == "PrimitivePlus":
            continue
        name = (legacy.get("Description") or class_name).strip()
        category = (legacy.get("Category") or "").strip() or None
        if kind == "creatures":
            category = category or "Creatures"
        elif kind == "engrams":
            category = "Engrams"
        merged[class_name] = {
            "name": name,
            "className": class_name,
            "category": category,
            "nameTag": (legacy.get("NameTag") or "").strip() or None,
            "mod": (legacy.get("Mod") or "ArkPrime").strip() or "ArkPrime",
            "asa": "unknown",
            "aliases": sorted({name, (legacy.get("NameTag") or "").strip()} - {"", None}),
        }

    for entry in wiki_entries:
        class_name = entry["className"]
        previous = merged.get(class_name, {})
        aliases = set(previous.get("aliases") or []) | set(entry.get("aliases") or [])
        merged[class_name] = {
            "name": entry.get("name") or previous.get("name") or class_name,
            "className": class_name,
            "category": entry.get("category") or previous.get("category"),
            "nameTag": entry.get("nameTag") or previous.get("nameTag"),
            "mod": entry.get("mod") or previous.get("mod") or "ArkPrime",
            "asa": entry.get("asa", "unknown"),
            "aliases": sorted(aliases),
        }

    return sorted(merged.values(), key=lambda item: (item["name"].lower(), item["className"].lower()))


def write_catalog(out: Path, name: str, source: str, entries: list[dict[str, Any]]) -> None:
    out.mkdir(parents=True, exist_ok=True)
    payload = {
        "source": source,
        "generatedAt": dt.date.today().isoformat(),
        "count": len(entries),
        "entries": entries,
    }
    path = out / f"{name}.json"
    path.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"wrote {path} ({len(entries)} entries)")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--legacy-dir", type=Path, default=DEFAULT_LEGACY)
    parser.add_argument("--out", type=Path, default=DEFAULT_OUT)
    parser.add_argument(
        "--skip-wiki",
        action="store_true",
        help="only use the legacy .gamedata files (offline)",
    )
    args = parser.parse_args()

    legacy = load_legacy(args.legacy_dir)
    if args.skip_wiki:
        creatures = merge([], legacy["creatures"], "creatures")
        engrams = merge([], legacy["engrams"], "engrams")
        items = merge([], legacy["items"], "items")
    else:
        creatures = merge(parse_creatures(), legacy["creatures"], "creatures")
        engrams = merge(parse_engrams(), legacy["engrams"], "engrams")
        items = merge(parse_items(), legacy["items"], "items")

    resources = [
        entry
        for entry in items
        if entry["category"] == "Resources"
        or entry["className"].startswith("PrimalItemResource")
        or entry["className"].startswith("PrimalItemConsumable_Mutagen")
    ]

    write_catalog(args.out, "creatures", CREATURE_URL, creatures)
    write_catalog(args.out, "engrams", ENGRAM_URL, engrams)
    write_catalog(args.out, "items", "Item_IDs + legacy game data", items)
    write_catalog(args.out, "resources", "Item_IDs/Resources + legacy game data", resources)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
