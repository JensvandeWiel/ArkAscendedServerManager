package eu.wynq.arkascendedservermanager.core.ini

import annotations.IniArray
import annotations.IniProperty
import annotations.IniSection
import annotations.IniSerializable
import annotations.IniStruct
import ArrayType
import kotlinx.serialization.Serializable
import serialization.IniSerializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@IniStruct
@Serializable
data class TestClassMultiplier(
    @IniProperty("ClassName")
    val className: String = "",
    @IniProperty("Multiplier")
    val multiplier: Float = 1.0f,
)

@IniSection("/Script/ShooterGame.ShooterGameMode")
@Serializable
data class TestTableGameMode(
    @IniProperty("DinoClassDamageMultipliers")
    @IniArray(arrayType = ArrayType.RepeatedLineArray)
    val damage: List<TestClassMultiplier> = emptyList(),
)

@IniSerializable
@Serializable
data class TestTableGame(
    @IniSection
    val mode: TestTableGameMode = TestTableGameMode(),
)

class TableRoundTripTest {

    @Test
    fun `repeated struct lines round-trip`() {
        val game = TestTableGame(
            TestTableGameMode(
                damage = listOf(
                    TestClassMultiplier("MegaRex_Character_BP_C", 0.1f),
                    TestClassMultiplier("Rex_Character_BP_C", 10.0f),
                )
            )
        )

        val text = IniSerializer.serialize(game)

        assertTrue(text.contains("DinoClassDamageMultipliers=(ClassName=\"MegaRex_Character_BP_C\""))
        assertTrue(text.contains("DinoClassDamageMultipliers=(ClassName=\"Rex_Character_BP_C\""))

        val parsed = IniSerializer.deserialize<TestTableGame>(text)
        assertEquals(game.mode.damage, parsed.mode.damage)
    }
}
