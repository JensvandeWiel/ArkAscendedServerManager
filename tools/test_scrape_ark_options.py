"""Tests for tools/scrape-ark-options.py.

Run with:  python -m unittest discover -s tools -p "test_*.py"
"""

from __future__ import annotations

import csv
import importlib.util
import tempfile
import unittest
from pathlib import Path

MODULE_PATH = Path(__file__).with_name("scrape-ark-options.py")
_spec = importlib.util.spec_from_file_location("scrape_ark_options", MODULE_PATH)
scrape = importlib.util.module_from_spec(_spec)
assert _spec.loader is not None
_spec.loader.exec_module(scrape)


FIXTURE = """\
==Command line options==
{{Server config variable
| name = -Foo
| type =
| default =
| cli = skip
| inASA = Yes
| inASE = No
| Does a thing.
| version = [[1.0]]
}}
{{Server config variable
| name = ?Bar=<value>
| type =
| default =
| cli = skip
| inASA = No
| inASE = Yes
| Does another thing, with a {{ItemLink|Rex}} in it.
| version = [[2.0]]
}}

==Configuration Files==
=== GameUserSettings.ini===
==== [ServerSettings] ====
{{Server config variable
| name = MultiLine
| type = string
| default = x
| cli = Yes
| inASA = Yes
| inASE = Yes
| info = first line
  second line with indentation
  third line
| version = {{gameicon|sa}}<br>
[[ARK: Survival Ascended/Patch/77.0|70.0]]
}}
{{Server config variable
| name = InlineClose
| type = boolean
| default = False
| cli = No
| inASA = Tes
| inASE = No
| info = ends inline
}}
{{Server config variable
| name = AfterInline
| type = boolean
| default = True
| cli = No
| inASA = Yes
| inASE = No
| info = must still be parsed
}}

===Game.ini===
====Creature Spawn related====
{{Server config variable
| name = DinoSpawnWeightMultipliers
| type = (...)
| default = N/A
| cli = skip
| inASA = Unknown
| inASE = Yes
| Repeated struct example.
}}

====[ModInstaller]====
{{Server config variable
| name = ModIDS
| type = ModID
| default =
| cli = Yes
| inASA = No
| inASE = Yes
| Legacy.
}}

== DynamicConfig ==
{{Server config variable
| name = XPMultiplier
| type = float
| default = 1.0
| cli = skip
| inASA = Yes
| inASE = Unknown
| Live value.
}}
"""


class ParseTests(unittest.TestCase):
    def setUp(self):
        self.rows, self.warnings = scrape.parse_rows(FIXTURE)
        self.by_name = {row["name"]: row for row in self.rows}

    def test_counts_and_order(self):
        self.assertEqual(len(self.rows), 8)
        self.assertEqual(self.rows[0]["name"], "-Foo")
        self.assertEqual(self.rows[-1]["name"], "XPMultiplier")

    def test_locations(self):
        self.assertEqual(self.by_name["-Foo"]["Location"], "Command line options")
        self.assertEqual(
            self.by_name["MultiLine"]["Location"],
            "Configuration Files > GameUserSettings.ini > [ServerSettings]",
        )
        # Game.ini grouping headings are collapsed, bracketed subsections are kept.
        self.assertEqual(self.by_name["DinoSpawnWeightMultipliers"]["Location"], "Configuration Files > Game.ini")
        self.assertEqual(self.by_name["ModIDS"]["Location"], "Configuration Files > Game.ini > [ModInstaller]")
        self.assertEqual(self.by_name["XPMultiplier"]["Location"], "DynamicConfig")

    def test_multiline_values_keep_indentation(self):
        info = self.by_name["MultiLine"]["info"]
        self.assertIn("first line", info)
        self.assertIn("\r\n  second line with indentation", info)
        self.assertTrue(self.by_name["MultiLine"]["version"].endswith("[[ARK: Survival Ascended/Patch/77.0|70.0]]"))

    def test_inline_close_does_not_swallow_next_template(self):
        self.assertEqual(self.by_name["InlineClose"]["info"], "ends inline")
        self.assertEqual(self.by_name["AfterInline"]["info"], "must still be parsed")

    def test_nested_template_is_kept_in_info(self):
        self.assertIn("{{ItemLink|Rex}}", self.by_name["?Bar=<value>"]["info"])

    def test_typo_normalisation_and_warning(self):
        self.assertEqual(self.by_name["InlineClose"]["inASA"], "Yes")
        self.assertTrue(any("Tes" in warning for warning in self.warnings))

    def test_cli_values_are_not_normalised(self):
        self.assertEqual(self.by_name["-Foo"]["cli"], "skip")


class ImplementedCarryOverTests(unittest.TestCase):
    def test_key_match_then_unique_name(self):
        old = [
            {"Location": "A", "name": "One", "implemented": "Yes"},
            {"Location": "B", "name": "Two", "implemented": "Yes"},
            {"Location": "C", "name": "Dup", "implemented": "Yes"},
            {"Location": "D", "name": "Dup", "implemented": ""},
        ]
        new = [
            {"Location": "A", "name": "One", "implemented": ""},
            {"Location": "Moved", "name": "Two", "implemented": ""},
            {"Location": "E", "name": "Dup", "implemented": ""},
        ]
        scrape.carry_over_implemented(new, old)
        self.assertEqual(new[0]["implemented"], "Yes")  # same key
        self.assertEqual(new[1]["implemented"], "Yes")  # unique name after relocation
        self.assertEqual(new[2]["implemented"], "")  # ambiguous name is not guessed


class DiffTests(unittest.TestCase):
    def test_added_removed_and_changed(self):
        old = [
            {"Location": "A", "name": "Same", "inASA": "Yes", "info": "text"},
            {"Location": "A", "name": "Gone", "inASA": "Yes", "info": "text"},
            {"Location": "A", "name": "Change", "inASA": "No", "info": "a\r\nb"},
        ]
        new = [
            {"Location": "A", "name": "Same", "inASA": "Yes", "info": "text"},
            {"Location": "A", "name": "New", "inASA": "Yes", "info": "text"},
            {"Location": "A", "name": "Change", "inASA": "Yes", "info": "a\nb"},
        ]
        added, removed, changed, _ = scrape.diff_rows(old, new)
        self.assertEqual(added, [("A", "New")])
        self.assertEqual(removed, [("A", "Gone")])
        # Only inASA changed; line-ending-only info changes are ignored.
        self.assertEqual(len(changed), 1)
        self.assertEqual([field[0] for field in changed[0][1]], ["inASA"])


class CsvRoundTripTests(unittest.TestCase):
    def test_write_then_read(self):
        rows, _ = scrape.parse_rows(FIXTURE)
        with tempfile.TemporaryDirectory() as tmp:
            path = Path(tmp) / "options.csv"
            scrape.write_csv(path, rows)
            again = scrape.read_existing(path)
            self.assertEqual(len(again), len(rows))
            self.assertEqual([row["name"] for row in again], [row["name"] for row in rows])
            self.assertEqual(again[3]["info"], rows[3]["info"])
            # Every row has the implemented column, even when empty.
            with path.open(encoding="utf-8", newline="") as handle:
                first = next(csv.reader(handle))
            self.assertEqual(first[-1], "implemented")

    def test_written_file_is_stable(self):
        rows, _ = scrape.parse_rows(FIXTURE)
        with tempfile.TemporaryDirectory() as tmp:
            one = Path(tmp) / "one.csv"
            two = Path(tmp) / "two.csv"
            scrape.write_csv(one, rows)
            parsed = scrape.read_existing(one)
            scrape.write_csv(two, parsed)
            self.assertEqual(one.read_bytes(), two.read_bytes())


if __name__ == "__main__":
    unittest.main()
