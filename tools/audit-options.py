#!/usr/bin/env python3
"""Audit ArkServerOptions.csv against the Kotlin models and the UI.

Produces:
    docs/option-audit.md   - human-readable report (summary + action list)
    docs/option-audit.csv  - one row per wiki option, for filtering

Run:
    python tools/audit-options.py
    python tools/audit-options.py --check   # exit 1 if action items exist

The script is intentionally heuristic. It parses the source with regular
expressions instead of compiling Kotlin; treat its output as a review aid, not
as a compiler.
"""

from __future__ import annotations

import argparse
import csv
import re
import sys
from collections import Counter, defaultdict
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parents[1]
CSV_PATH = REPO_ROOT / "ArkServerOptions.csv"
OUT_MD = REPO_ROOT / "docs" / "option-audit.md"
OUT_CSV = REPO_ROOT / "docs" / "option-audit.csv"

INI_FILES = [
    REPO_ROOT / "core/src/main/kotlin/ini/GameUserSettings.kt",
    REPO_ROOT / "core/src/main/kotlin/ini/Game.kt",
]
CLI_FILE = REPO_ROOT / "core/src/main/kotlin/server/Settings.kt"
UI_DIR = REPO_ROOT / "ui/src/main/kotlin"

CLASS_LOCATIONS = {
    "SessionSettings": "Configuration Files > GameUserSettings.ini > [SessionSettings]",
    "MessageOfTheDay": "Configuration Files > GameUserSettings.ini > [MessageOfTheDay]",
    "ServerSettings": "Configuration Files > GameUserSettings.ini > [ServerSettings]",
    "ShooterGameMode": "Configuration Files > Game.ini",
}
CLASS_FILES = {
    "SessionSettings": "GameUserSettings.kt",
    "MessageOfTheDay": "GameUserSettings.kt",
    "ServerSettings": "GameUserSettings.kt",
    "ShooterGameMode": "Game.kt",
}

# Options the app deliberately sets on the launch line although the wiki lists
# them under [ServerSettings].
CLI_COVERED = {
    "port", "queryport", "serverpassword", "serveradminpassword", "rconport",
    "activemods",  # until the mods model is switched to ActiveMods
}

# Defaults that are known/intentional deviations; do not report them as bugs.
DEFAULT_EXCEPTIONS = {
    "SessionName": "branding default",
    "TribeNameChangeCooldown": "unit ambiguity, tracked in the plan",
    "AllowCaveBuildingPvP": "fix planned (ASA default True)",
    "HairGrowthSpeedMultiplier": "fix planned (ASA default 0)",
    "NoTributeDownloads": "fresh servers default to closed transfers",
    "PreventDownloadDinos": "fresh servers default to closed transfers",
    "PreventDownloadItems": "fresh servers default to closed transfers",
    "PreventDownloadSurvivors": "fresh servers default to closed transfers",
    "PreventUploadDinos": "fresh servers default to closed transfers",
    "PreventUploadItems": "fresh servers default to closed transfers",
    "PreventUploadSurvivors": "fresh servers default to closed transfers",
    "RCONEnabled": "manager keeps RCON on by default",
}

# The app intentionally sets these on the launch line although the wiki lists
# them under [ServerSettings]. There must be exactly one source of truth.
EXPECTED_CLI_LOCATIONS = {
    "port", "queryport", "serverpassword", "serveradminpassword",
    "rconport", "rconenabled",
}

# ARK documents these as floats but the value is a whole number; the model uses
# Int on purpose (see the plan's unit notes).
KNOWN_TYPE_DOWNGRADES = {
    "autosaveperiodminutes", "kickidleplayersperiod", "rconservergamelogbuffer",
    "maxtameddinos", "tribenamechangecooldown", "minimumdinoreuploadinterval",
    "tribeslotreusecooldown",
}


def norm(name: str) -> str:
    value = (name or "").strip()
    value = value.lstrip("?-")
    value = value.split("=")[0]
    value = value.replace("'''", "")
    value = re.sub(r"\[.*$", "", value)
    return value.strip().lower()


def read_csv(path: Path) -> list[dict[str, str]]:
    with path.open(encoding="utf-8-sig", newline="") as handle:
        return list(csv.DictReader(handle))


def class_blocks(text: str):
    """Yield (class_name, block_text) for each data class."""
    matches = list(re.finditer(r"(?:@IniSection\([^)]*\)\s*)?data class (\w+)\s*\(", text))
    for index, match in enumerate(matches):
        start = match.start()
        end = matches[index + 1].start() if index + 1 < len(matches) else len(text)
        yield match.group(1), text[start:end]


def parse_ini_models():
    entries = []
    for path in INI_FILES:
        text = path.read_text(encoding="utf-8")
        for class_name, block in class_blocks(text):
            pattern = re.compile(
                r"(?P<annos>(?:^\s*@[^\n]*\n)*)"
                r"^\s*val (?P<field>\w+): (?P<type>[^=\n]+?)"
                r"(?: = (?P<default>[^\n,]+))?,?\s*$",
                re.MULTILINE,
            )
            for match in pattern.finditer(block):
                annos = match.group("annos")
                prop = re.search(r'@IniProperty\("([^"]+)"\)', annos)
                if not prop:
                    continue
                entries.append(
                    {
                        "name": prop.group(1),
                        "field": match.group("field"),
                        "type": match.group("type").strip(),
                        "default": (match.group("default") or "").strip(),
                        "unsure": "Unsure" in annos,
                        "omit_if_null": "@OmitIfNull" in annos,
                        "class": class_name,
                        "file": CLASS_FILES.get(class_name, path.name),
                        "location": CLASS_LOCATIONS.get(class_name, "?"),
                        "source": path.name,
                    }
                )
    return entries


def parse_cli_models():
    text = CLI_FILE.read_text(encoding="utf-8")
    entries = []
    pattern = re.compile(
        r"(?P<annos>(?:^\s*@(?!field:ArgOption)[^\n]*\n)*)"
        r'^\s*@field:ArgOption\(name = "([^"]+)"(?:,\s*kind = ArgKind\.(\w+))?'
        r'(?:[^)]*)\)\s*\n\s*val (\w+): ([^=\n]+?)(?: = ([^\n,]+))?,',
        re.MULTILINE,
    )
    for match in pattern.finditer(text):
        entries.append(
            {
                "name": match.group(2),
                "kind": match.group(3) or "QUERY",
                "field": match.group(4),
                "type": match.group(5).strip(),
                "default": (match.group(6) or "").strip(),
                "unsure": "Unsure" in match.group("annos"),
                "class": "Administration/Options",
                "file": "Settings.kt",
                "location": "Command line options",
                "source": "Settings.kt",
            }
        )
    return entries


def ui_text() -> str:
    chunks = []
    for path in UI_DIR.rglob("*.kt"):
        chunks.append(path.read_text(encoding="utf-8"))
    return "\n".join(chunks)


def simple_default(value: str):
    """Return a comparable default, or None when the expression is not simple."""
    value = (value or "").strip()
    if not value:
        return None
    if value in ("null", "emptyList()", "emptySet()", "emptyMap()"):
        return None
    if any(token in value for token in ("(", ")", "*", "/", "+", "getRandomString")):
        return None
    if value in ("true", "false"):
        return value.capitalize()
    value = value.removesuffix("f").removesuffix("F")
    value = value.strip('"')
    try:
        number = float(value)
        return str(int(number)) if number.is_integer() and "." not in value else str(number)
    except ValueError:
        return value


def csv_default(value: str):
    value = (value or "").strip()
    if not value or value in ("N/A", "Unknown") or value.startswith("{{") or "<" in value:
        return None
    if value == "False":
        return "False"
    if value == "True":
        return "True"
    try:
        number = float(value)
        return str(int(number)) if number.is_integer() and "." not in value else str(number)
    except ValueError:
        return value


def defaults_equal(left: str, right: str) -> bool:
    if left == right:
        return True
    try:
        return float(left) == float(right)
    except (TypeError, ValueError):
        return False


def type_family(csv_type: str):
    value = (csv_type or "").lower()
    if "boolean" in value:
        return "boolean"
    if "float" in value or "multiplier" in value:
        return "float"
    if "integer" in value or "seconds" in value or value == "value" or "modid" in value:
        return "integer"
    if "string" in value or "url" in value or "ip_address" in value:
        return "string"
    return None


def kotlin_family(kotlin_type: str):
    value = (kotlin_type or "").strip().rstrip("?")
    return {
        "Boolean": "boolean",
        "Float": "float",
        "Int": "integer",
        "String": "string",
    }.get(value)


def is_repeated_or_struct(row: dict[str, str]) -> bool:
    return "(...)" in (row.get("type") or "") or "[" in (row.get("name") or "") or "(" in (row.get("name") or "")


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--check", action="store_true", help="exit 1 when action items exist")
    parser.add_argument("--fix-implemented", action="store_true", help="set implemented=Yes for exposed, modelled options and rewrite the CSV")
    args = parser.parse_args()

    rows = read_csv(CSV_PATH)
    ini = parse_ini_models()
    cli = parse_cli_models()
    ui = ui_text()

    model_by_norm: dict[str, list[dict]] = defaultdict(list)
    for entry in ini + cli:
        model_by_norm[norm(entry["name"])].append(entry)

    model_norms = set(model_by_norm)
    issues = []
    flags = []
    matrix = []
    sections = Counter()

    for row in rows:
        location = row.get("Location", "")
        name = row.get("name", "")
        key = norm(name)
        status = "missing"
        entries = model_by_norm.get(key, [])
        entry = entries[0] if entries else None

        if row.get("inASA") == "No":
            status = "ase-only"
        elif location == "DynamicConfig":
            status = "dynamic-config"
        elif entry:
            status = f"modeled-{entry['file'].replace('.kt', '').lower()}"
        elif key in CLI_COVERED and (row.get("Location") or "").startswith("Configuration"):
            status = "covered-by-cli"
        elif key == "mods":
            # ASA mods are stored in ActiveMods; -mods is legacy import only.
            status = "covered-by-activemods"
        elif is_repeated_or_struct(row):
            status = "table-missing"
        elif row.get("inASA") == "Yes":
            status = "missing-asa"
        elif row.get("inASA") == "Unknown":
            status = "missing-unknown"

        sections[status] += 1
        row_issues = []
        row_flags = []

        if entry and location != "DynamicConfig":
            # 1. ASA support
            if row.get("inASA") == "No":
                row_issues.append("implemented but wiki says ASA=No")
            if row.get("inASA") == "Unknown" and not entry["unsure"]:
                row_issues.append("ASA=Unknown but property lacks @Unsure")

            # 2. Location / file
            if entry["file"] == "Settings.kt":
                if location.startswith("Configuration") and key not in EXPECTED_CLI_LOCATIONS:
                    row_issues.append(f"modeled as launch arg but CSV location is {location}")
                elif name.startswith("?") and entry["kind"] not in ("QUERY", "BOOLEAN_QUERY"):
                    row_issues.append(f"CSV query option modeled as {entry['kind']}")
                elif name.startswith("-") and key not in EXPECTED_CLI_LOCATIONS and entry["kind"] not in ("FLAG", "FLAG_VALUE", "LIST"):
                    row_issues.append(f"CSV dash option modeled as {entry['kind']}")
            else:
                if location != entry["location"]:
                    if location.startswith("Command line"):
                        row_issues.append(f"modeled in {entry['file']} but CSV is a command line option")
                    elif location.startswith("Configuration") and entry["file"] == "Game.kt":
                        row_issues.append("modeled in Game.ini but CSV says GameUserSettings.ini")
                    elif location.startswith("Configuration") and entry["file"] == "GameUserSettings.kt":
                        row_issues.append("modeled in GameUserSettings.ini but CSV says Game.ini")
                    else:
                        row_issues.append(f"location mismatch: CSV {location!r}, model {entry['location']!r}")

            # 3. Default drift
            if key not in {exception.lower() for exception in DEFAULT_EXCEPTIONS}:
                left = csv_default(row.get("default", ""))
                right = simple_default(entry["default"])
                if left is not None and right is not None and not defaults_equal(left, right):
                    row_issues.append(f"default mismatch: CSV {left!r}, model {right!r}")

            # 4. Type
            csv_family = type_family(row.get("type", ""))
            kotlin_family_value = kotlin_family(entry["type"])
            if (
                csv_family
                and kotlin_family_value
                and csv_family != kotlin_family_value
                and key not in KNOWN_TYPE_DOWNGRADES
            ):
                row_issues.append(f"type mismatch: CSV {csv_family}, model {kotlin_family_value}")

            # 5. UI exposure
            exposed = bool(re.search(rf"\.{re.escape(entry['field'])}\b", ui))
            if not exposed:
                row_issues.append("no UI reference")

            # 6. Implemented flag (stale flags are tracked separately)
            if exposed and (row.get("implemented") or "").strip().lower() != "yes":
                row_flags.append("exposed in UI but implemented != Yes")

        if row_issues:
            issues.append((row, entry, row_issues))
        if row_flags:
            flags.append((row, entry, row_flags))

        matrix.append(
            {
                "Location": location,
                "name": name,
                "inASA": row.get("inASA", ""),
                "inASE": row.get("inASE", ""),
                "type": row.get("type", ""),
                "default": row.get("default", ""),
                "status": status,
                "model_field": entry["field"] if entry else "",
                "model_file": entry["file"] if entry else "",
                "model_type": entry["type"] if entry else "",
                "model_default": entry["default"] if entry else "",
                "unsure": "yes" if entry and entry["unsure"] else "",
                "implemented": row.get("implemented", ""),
                "issues": " | ".join(row_issues),
                "flag": " | ".join(row_flags),
            }
        )

    # Optionally reconcile the stale implemented column in one pass.
    if args.fix_implemented and flags:
        fixed_keys = {(row["Location"], row["name"]) for row, _, _ in flags}
        for row, _, _ in flags:
            row["implemented"] = "Yes"
        with CSV_PATH.open("w", encoding="utf-8", newline="") as handle:
            writer = csv.DictWriter(handle, fieldnames=list(rows[0].keys()), lineterminator="\r\n")
            writer.writeheader()
            writer.writerows(rows)
        for item in matrix:
            if (item["Location"], item["name"]) in fixed_keys:
                item["implemented"] = "Yes"
                item["flag"] = ""
        print(f"marked {len(fixed_keys)} options implemented=Yes")
        flags = []

    # Model entries that the wiki table does not contain.
    app_only = [entry for entry in ini + cli if not any(norm(row["name"]) == norm(entry["name"]) for row in rows)]

    # Write the full matrix.
    with OUT_CSV.open("w", encoding="utf-8", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=list(matrix[0].keys()), lineterminator="\r\n")
        writer.writeheader()
        writer.writerows(matrix)

    # Write the report.
    total = len(rows)
    asa = Counter(row.get("inASA", "") for row in rows)
    lines = []
    lines.append("# Option audit")
    lines.append("")
    lines.append("Generated by `tools/audit-options.py`. This is a review aid, not a compiler.")
    lines.append("")
    lines.append("## Summary")
    lines.append("")
    lines.append(f"- Wiki rows: **{total}** ({asa.get('Yes', 0)} ASA yes, {asa.get('Unknown', 0)} unknown, {asa.get('No', 0)} no)")
    lines.append(f"- Modelled options: **{len(ini) + len(cli)}** ({len(ini)} INI, {len(cli)} launch)")
    for key, count in sorted(sections.items()):
        lines.append(f"- {key}: **{count}**")
    lines.append(f"- Action items: **{len(issues)}**")
    lines.append(f"- Stale `implemented` flags: **{len(flags)}** (expected until phase 0)")
    lines.append(f"- Full matrix: [`option-audit.csv`](option-audit.csv)")
    lines.append("")

    lines.append("## Action required")
    lines.append("")
    if not issues:
        lines.append("No contradictions found.")
    else:
        lines.append("| Option | CSV | Model | Issue |")
        lines.append("|---|---|---|---|")
        for row, entry, row_issues in issues:
            where = f"{entry['file']}:{entry['field']}" if entry else "-"
            csv_where = f"{row.get('Location', '')} / ASA={row.get('inASA', '')}"
            lines.append(
                f"| `{row.get('name', '')}` | {csv_where} | {where} | {'; '.join(row_issues)} |"
            )
    lines.append("")

    lines.append("## Stale implemented flags")
    lines.append("")
    lines.append("These options are modelled and exposed in the UI but the CSV does not say `implemented=Yes`.")
    lines.append("They are not correctness bugs; phase 0 will reconcile the column.")
    lines.append("")
    if flags:
        lines.append(f"<details><summary>Show {len(flags)} options</summary>")
        lines.append("")
        lines.append("| Option | Model |")
        lines.append("|---|---|")
        for row, entry, _ in flags:
            where = f"{entry['file']}:{entry['field']}" if entry else "-"
            lines.append(f"| `{row.get('name', '')}` | {where} |")
        lines.append("")
        lines.append("</details>")
    else:
        lines.append("None.")
    lines.append("")

    lines.append("## Modelled options")
    lines.append("")
    lines.append("| Option | File | Type | Default | ASA | UI | Unsure | Implemented | Issues |")
    lines.append("|---|---|---|---|---|---|---|---|---|")
    by_model_key = defaultdict(list)
    for item in matrix:
        if item["model_field"]:
            by_model_key[(item["model_file"], item["model_field"])].append(item)
    for entry in sorted(ini + cli, key=lambda item: (item["file"], item["field"])):
        matches = by_model_key.get((entry["file"], entry["field"]), [])
        row = matches[0] if matches else None
        exposed = bool(re.search(rf"\.{re.escape(entry['field'])}\b", ui))
        issues_for = row["issues"] if row else "not in wiki table"
        lines.append(
            "| `{}` | {} | {} | {} | {} | {} | {} | {} | {} |".format(
                entry["name"],
                entry["file"],
                entry["type"],
                entry["default"] or "-",
                row["inASA"] if row else "-",
                "yes" if exposed else "**no**",
                "yes" if entry["unsure"] else "",
                row["implemented"] if row else "",
                issues_for,
            )
        )
    lines.append("")

    lines.append("## Missing, ASA=Yes (scalar)")
    lines.append("")
    lines.append("These are the real gaps; table/struct options are listed separately below.")
    lines.append("")
    lines.append("| Option | Location | Type | Default |")
    lines.append("|---|---|---|---|")
    for item in matrix:
        if item["status"] == "missing-asa" and not is_repeated_or_struct(item):
            lines.append(
                f"| `{item['name']}` | {item['Location']} | {item['type'] or '-'} | {item['default'] or '-'} |"
            )
    lines.append("")

    lines.append("## Table / struct options")
    lines.append("")
    lines.append("| Option | Location | ASA |")
    lines.append("|---|---|---|")
    for item in matrix:
        if item["status"] == "table-missing":
            lines.append(f"| `{item['name']}` | {item['Location']} | {item['inASA']} |")
    lines.append("")

    lines.append("## App-only settings (not in the wiki table)")
    lines.append("")
    lines.append("| Option | File | Type | Default |")
    lines.append("|---|---|---|---|")
    for entry in sorted(app_only, key=lambda item: (item["file"], item["field"])):
        lines.append(f"| `{entry['name']}` | {entry['file']} | {entry['type']} | {entry['default'] or '-'} |")
    lines.append("")
    lines.append("These are manager features (Discord, restart policy, cluster model, ...), not ARK keys.")
    lines.append("")

    lines.append("## Notes")
    lines.append("")
    lines.append("- `status=covered-by-cli` means the wiki lists the key under `[ServerSettings]` but the app sets it on the launch line (ports, passwords). That is intentional, but it must never be written in both places.")
    lines.append("- `status=missing-unknown` options are candidates for an experimental group; do not ship them as normal settings.")
    lines.append("- Table options are tracked in `docs/server-configuration-parity-plan.md` §7.")
    lines.append("- Stale `implemented` flags are expected until phase 0 finishes; the scraper preserves them.")
    lines.append("")

    OUT_MD.write_text("\n".join(lines) + "\n", encoding="utf-8")

    print(f"wiki rows: {total}")
    print(f"modelled: {len(ini)} INI + {len(cli)} launch")
    print(f"action items: {len(issues)}")
    print(f"stale implemented flags: {len(flags)}")
    print(f"status: {dict(sections)}")
    print(f"wrote {OUT_MD.relative_to(REPO_ROOT)} and {OUT_CSV.relative_to(REPO_ROOT)}")
    if issues and args.check:
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
