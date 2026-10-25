# ASA class-name catalogs

The JSON files in `core/src/main/resources/catalogs/asa/` are generated data, not source:

| File | Used by |
|---|---|
| `creatures.json` | dino class multipliers, spawn weights, NPC replacements, prevent-breed/tame |
| `engrams.json` | named engram overrides, engram auto-unlocks |
| `items.json` | item stack overrides |
| `resources.json` | harvest resource multipliers |

## Regenerate

```powershell
pip install requests beautifulsoup4
python tools/scrape-ark-catalogs.py
```

The script merges:

- the legacy ARK Server Manager `ARKServerManager/GameData/*.gamedata` files, which contain the
  ASE-era class names, name tags, categories and mod IDs;
- the current [Creature IDs](https://ark.wiki.gg/wiki/Creature_IDs) and
  [Engram class names](https://ark.wiki.gg/wiki/Engram_class_names) pages;
- the `Item IDs/*` subpages for item and resource class names.

`PrimitivePlus` and `PGM` data is excluded. Entries found on an ASA-specific wiki page are marked
`asa=yes`; entries that only exist in the legacy data are `asa=unknown` and the picker labels them
"Not ASA-verified". Free-text class names are always allowed, so modded or brand-new content is
never blocked by the catalog.

Wiki content is licensed [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/). The JSON
`source` field records where each catalog came from.
