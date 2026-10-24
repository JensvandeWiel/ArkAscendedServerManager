@./skills/caveman/SKILL.md
@./skills/add-options/SKILL.md

# Project rules

- Target **ARK: Survival Ascended only**. Do not implement an option whose `inASA` is `No`.
  `inASA=Unknown` requires `@Unsure` and an experimental UI grouping; never present it as verified.
- Read `docs/server-configuration-parity-plan.md` before adding or changing options. It contains
  the tab map, the table/struct design, the test strategy and the roadmap.
- The legacy C# manager (`C:\Users\jens\MyProjects\ServerManagers`) is a **third source of
  truth**. It is ASE-era and can contradict the wiki; `tools/audit-options.py` reports
  legacy-only keys and location conflicts. Do not resolve them silently.
- `ArkServerOptions.csv` is generated. Run `python tools/scrape-ark-options.py`; never hand-append.
  The scraper preserves the `implemented` column.
- Run `python tools/audit-options.py --check` after changing the model, CSV or UI. It must report
  **0 action items**. Use `--fix-implemented` when an option is exposed but not marked.
- Mods are stored in `GameUserSettings.ini > [ServerSettings] > ActiveMods` as CurseForge project
  IDs. `-mods=` is legacy import only unless `mirrorModsToLaunchArgs` is enabled (default off;
  plan §0.1). Both routes work in ASA; keep the lists identical if mirroring.
- inisaur: a sibling checkout (`../inisaur`) is used via a conditional composite build when
  present. Keep `withActiveModsFromRaw` until a release with the single-item-array fix is
  published; then remove it.
- Tests: `python -m unittest discover -s tools -p "test_*.py"` and
  `./gradlew :core:test :ui:compileKotlin -x :overseer:cargoBuild` (the native `overseer` build
  currently fails on `conpty.lib`). Existing behavior is covered by `ServerIoTest`,
  `IniRoundTripTest`, `SettingsStartupScriptTest` and the fake RCON server tests; keep them green.
- If Compose resource accessors fail with unresolved `_collectMainString*` after adding strings,
  delete `ui/build/generated/compose` and rebuild; it is a resource-generator cache issue, not a
  code error.
- Preserve unknown INI keys and user custom config. `Config Editor` must always round-trip.
- Report option work with: CSV name, file/section, default, validation, UI placement, test added,
  and `implemented=Yes`.
