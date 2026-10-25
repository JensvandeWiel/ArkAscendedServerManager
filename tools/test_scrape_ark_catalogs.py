import importlib.util
import unittest
from pathlib import Path


def load_module():
    path = Path(__file__).with_name("scrape-ark-catalogs.py")
    spec = importlib.util.spec_from_file_location("scrape_ark_catalogs", path)
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


class CatalogScraperTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.module = load_module()

    def test_class_from_blueprint(self):
        blueprint = "Blueprint'/Game/PrimalEarth/Dinos/Rex/Rex_Character_BP.Rex_Character_BP'"
        self.assertEqual("Rex_Character_BP_C", self.module.class_from_blueprint(blueprint))

    def test_mod_from_blueprint(self):
        blueprint = "Blueprint'/Game/Mods/123456/MyDino/MyDino_Character_BP.MyDino_Character_BP'"
        self.assertEqual("123456", self.module.mod_from_blueprint(blueprint))

    def test_merge_prefers_asa_wiki_entry_and_keeps_legacy_alias(self):
        wiki = [
            {
                "name": "Rex",
                "className": "Rex_Character_BP_C",
                "category": "Dinosaurs",
                "nameTag": "Rex",
                "mod": "ArkPrime",
                "asa": "yes",
                "aliases": ["Rex"],
            }
        ]
        legacy = [
            {
                "ClassName": "Rex_Character_BP_C",
                "Description": "Rex",
                "NameTag": "Rex",
                "Mod": "ArkPrime",
            },
            {
                "ClassName": "Old_ASE_Only_C",
                "Description": "Old ASE Only",
                "NameTag": "Old",
                "Mod": "ArkPrime",
            },
        ]

        merged = {entry["className"]: entry for entry in self.module.merge(wiki, legacy, "creatures")}

        self.assertEqual("yes", merged["Rex_Character_BP_C"]["asa"])
        self.assertEqual("unknown", merged["Old_ASE_Only_C"]["asa"])

    def test_merge_excludes_primitive_plus(self):
        legacy = [
            {
                "ClassName": "PrimitivePlusOnly_C",
                "Description": "Primitive Plus only",
                "Mod": "PrimitivePlus",
            }
        ]

        merged = self.module.merge([], legacy, "items")

        self.assertEqual([], merged)


if __name__ == "__main__":
    unittest.main()
