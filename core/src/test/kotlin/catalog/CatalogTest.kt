package eu.wynq.arkascendedservermanager.core.catalog

import kotlin.test.Test
import kotlin.test.assertTrue

class CatalogTest {

    @Test
    fun `creature catalog contains verified ASA class names`() {
        val rex = ArkCatalogs.creatures.firstOrNull { it.className == "Rex_Character_BP_C" }

        assertTrue(rex != null, "Rex_Character_BP_C should be in the creature catalog")
        assertTrue(rex.isAsaVerified, "Rex should be marked as ASA-verified")
        assertTrue(rex.nameTag == "Rex")
    }

    @Test
    fun `engram catalog contains the stone hatchet`() {
        val hatchet = ArkCatalogs.engrams.firstOrNull {
            it.className == "EngramEntry_StoneHatchet_C"
        }

        assertTrue(hatchet != null, "EngramEntry_StoneHatchet_C should be in the engram catalog")
        assertTrue(hatchet.isAsaVerified)
    }

    @Test
    fun `resource catalog contains thatch`() {
        val thatch = ArkCatalogs.resources.firstOrNull {
            it.className == "PrimalItemResource_Thatch_C"
        }

        assertTrue(thatch != null, "PrimalItemResource_Thatch_C should be in the resource catalog")
    }

    @Test
    fun `search matches names class names tags and aliases`() {
        val results = ArkCatalogs.search(ArkCatalogs.creatures, "mega rex")

        assertTrue(results.any { it.className == "MegaRex_Character_BP_C" })
    }
}
