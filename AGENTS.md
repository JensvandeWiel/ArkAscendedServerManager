@./skills/caveman/SKILL.md
@./skills/add-options/SKILL.md

# Project rules

- Target **ARK: Survival Ascended only**. Do not implement an option whose `inASA` is `No`.
  `inASA=Unknown` requires `@Unsure` and an experimental UI grouping; never present it as verified.
- Read `docs/server-configuration-parity-plan.md` before adding or changing options. It contains
  the tab map, the table/struct design, the test strategy and the roadmap.
- `ArkServerOptions.csv` is generated. Run `python tools/scrape-ark-options.py`; never hand-append.
  The scraper preserves the `implemented` column.
- Run `python tools/audit-options.py --check` after changing the model, CSV or UI. It must report
  **0 action items**. Use `--fix-implemented` when an option is exposed but not marked.
- Mods are stored in `GameUserSettings.ini > [ServerSettings] > ActiveMods` as CurseForge project
  IDs. `-mods=` is legacy import only; do not emit it without an explicit decision (plan §0.1).
  A `-mods` mirror toggle is the documented override if a real server needs it.
- Tests: `python -m unittest discover -s tools -p "test_*.py"` and
  `./gradlew :core:test :ui:test -x :overseer:cargoBuild` (the native `overseer` build currently
  fails on `conpty.lib`).
- Preserve unknown INI keys and user custom config. `Config Editor` must always round-trip.
- Report option work with: CSV name, file/section, default, validation, UI placement, test added,
  and `implemented=Yes`.
