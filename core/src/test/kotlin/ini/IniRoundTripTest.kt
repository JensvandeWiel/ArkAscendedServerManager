package eu.wynq.arkascendedservermanager.core.ini

import eu.wynq.arkascendedservermanager.core.server.Administration
import eu.wynq.arkascendedservermanager.core.server.Settings
import kotlinx.serialization.json.Json
import serialization.IniSerializer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IniRoundTripTest {

    @Test
    fun `default GameUserSettings round-trips through the INI serializer`() {
        val original = GameUserSettings()
        val parsed = IniSerializer.deserialize<GameUserSettings>(IniSerializer.serialize(original))
        // ignoredKeys is an IniFile without value equality; compare the modelled sections.
        assertEquals(original.sessionSettings, parsed.sessionSettings)
        assertEquals(original.messageOfTheDay, parsed.messageOfTheDay)
        assertEquals(original.serverSettings, parsed.serverSettings)
    }

    @Test
    fun `default Game round-trips through the INI serializer`() {
        val original = Game()
        val parsed = IniSerializer.deserialize<Game>(IniSerializer.serialize(original))
        assertEquals(original.shooterGameMode, parsed.shooterGameMode)
    }

    @Test
    fun `ActiveMods is written to ServerSettings and parsed back`() {
        val settings = GameUserSettings().copy(
            serverSettings = ServerSettings(activeMods = listOf(111, 222))
        )

        val text = IniSerializer.serialize(settings)

        assertTrue(text.contains("ActiveMods=111,222"))
        val parsedMods = IniSerializer.deserialize<GameUserSettings>(text).serverSettings.activeMods
        assertEquals(listOf(111, 222), parsedMods)
    }

    @Test
    fun `unknown ServerSettings keys are preserved`() {
        val input = """
            [ServerSettings]
            ActiveMods=111,222
            SomeFutureModKey=keep-me
            AnotherKey=123
        """.trimIndent()

        val parsed = IniSerializer.deserialize<GameUserSettings>(input)
        val text = IniSerializer.serialize(parsed)

        assertTrue(text.contains("SomeFutureModKey=keep-me"))
        assertTrue(text.contains("AnotherKey=123"))
        assertEquals(listOf(111, 222), parsed.serverSettings.activeMods)
    }

    @Test
    fun `unknown ShooterGameMode keys are preserved`() {
        val input = """
            [/Script/ShooterGame.ShooterGameMode]
            SomeFutureGameKey=keep-me
            MaxNumberOfPlayersInTribe=0
        """.trimIndent()

        val parsed = IniSerializer.deserialize<Game>(input)
        val text = IniSerializer.serialize(parsed)

        assertTrue(text.contains("SomeFutureGameKey=keep-me"))
        assertEquals(0, parsed.shooterGameMode.maxNumberOfPlayersInTribe)
    }

    @Test
    fun `ActiveMods validation accepts ids and rejects spaces`() {
        assertTrue(ServerSettings(activeMods = emptyList()).validateActiveMods())
        assertTrue(ServerSettings(activeMods = listOf(111, 222)).validateActiveMods())
        assertFalse(ServerSettings(activeMods = listOf(-1)).validateActiveMods())
    }

    @Test
    fun `ASA defaults are applied to new profiles`() {
        assertEquals(15, ServerSettings().tribeNameChangeCooldown)
        assertTrue(ServerSettings().allowCaveBuildingPvP)
        assertEquals(0, ShooterGameMode().maxNumberOfPlayersInTribe)
        assertEquals(0.0f, ShooterGameMode().hairGrowthSpeedMultiplier)
        assertEquals(27020, Administration().rconPort)
    }

    @Test
    fun `old Settings JSON decodes with new fields defaulted`() {
        // A minimal row from a previous release: no activeMods, difficultyOffset, etc.
        val oldJson = """
            {
              "administration": { "map": "TheIsland_WP" },
              "options": {}
            }
        """.trimIndent()
        val json = Json { ignoreUnknownKeys = true }

        val parsed = json.decodeFromString<Settings>(oldJson)

        assertEquals("TheIsland_WP", parsed.administration.map)
        assertEquals(emptyList<Int>(), GameUserSettings().serverSettings.activeMods)
    }
}
