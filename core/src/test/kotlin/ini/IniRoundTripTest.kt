package eu.wynq.arkascendedservermanager.core.ini

import eu.wynq.arkascendedservermanager.core.server.Administration
import eu.wynq.arkascendedservermanager.core.server.Settings
import eu.wynq.arkascendedservermanager.core.support.isValidUrl
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
    fun `existing GameUserSettings values round-trip`() {
        val settings = GameUserSettings().copy(
            sessionSettings = SessionSettings("Roundtrip Server"),
            messageOfTheDay = MessageOfTheDay(duration = 42, message = "hello"),
            serverSettings = ServerSettings(
                autoSavePeriodMinutes = 5,
                activeMods = listOf(111, 222),
                difficultyOffset = 2.5f,
                kickIdlePlayersPeriod = 1200,
                rconServerGameLogBuffer = 300,
                maxTributeDinos = 30,
                maxTributeItems = 60,
                tamingSpeedMultiplier = 2.0f,
                xpMultiplier = 3.0f,
                serverPVE = true,
                allowCaveBuildingPvP = false,
                tribeNameChangeCooldown = 60,
                maxTamedDinos = 1234,
                dayCycleSpeedScale = 2.0f,
            )
        )

        val parsed = IniSerializer.deserialize<GameUserSettings>(IniSerializer.serialize(settings))

        assertEquals(settings.sessionSettings, parsed.sessionSettings)
        assertEquals(settings.messageOfTheDay, parsed.messageOfTheDay)
        assertEquals(settings.serverSettings, parsed.serverSettings)
    }

    @Test
    fun `existing ShooterGameMode values round-trip`() {
        val mode = ShooterGameMode(
            genericXPMultiplier = 2.0f,
            disableFriendlyFire = true,
            useSingleplayerSettings = true,
            tribeSlotReuseCooldown = 3600,
            maxNumberOfPlayersInTribe = 4,
            pveAllowTribeWar = false,
            customRecipeEffectivenessMultiplier = 3.0f,
            dinoHarvestingDamageMultiplier = 1.5f,
        )

        val parsed = IniSerializer.deserialize<Game>(IniSerializer.serialize(Game(shooterGameMode = mode)))

        assertEquals(mode, parsed.shooterGameMode)
    }

    @Test
    fun `existing validation rules reject bad values`() {
        assertFalse(Administration(serverPort = 0).validate())
        assertFalse(Administration(serverPort = 65535).validate()) // peer port overflows
        assertFalse(Administration(adminPassword = "").validate())
        assertFalse(Administration(slots = 0).validate())
        assertFalse(Administration(mods = listOf("abc")).validate())
        assertFalse(Administration(culture = "xx").validate())
        assertFalse(Administration(activeEvent = "Two Words").validate())
        assertTrue(Administration().validate())

        assertFalse(ServerSettings(autoSavePeriodMinutes = -1).validate())
        assertFalse(ServerSettings(kickIdlePlayersPeriod = -1).validate())
        assertFalse(ServerSettings(maxTributeDinos = 10).validate())
        assertFalse(ServerSettings(maxTributeItems = 10).validate())
        assertFalse(ServerSettings(banListUrl = "not-a-url").validate())
        assertFalse(ServerSettings(badWordListUrl = "not-a-url").validateBadWordListUrl())
        assertFalse(ServerSettings(badWordWhiteListUrl = "not-a-url").validateBadWordWhiteListUrl())
        assertFalse(ServerSettings(adminListUrl = "not-a-url").validateAdminListUrl())
        assertFalse(ServerSettings(cosmeticWhitelistOverride = "not-a-url").validateCosmeticWhitelistOverride())
        assertFalse(ServerSettings(updateAllowedCheatersInterval = -1.0f).validateUpdateAllowedCheatersInterval())
        assertFalse(ServerSettings(structureResistanceMultiplier = -1.0f).validate())
        assertFalse(ServerSettings(theMaxStructuresInRange = -1).validate())
        assertFalse(ShooterGameMode(limitTurretsNum = -1).validate())
        assertTrue(ServerSettings().validate())
    }

    @Test
    fun `single ActiveMods value is recovered from raw text`() {
        val raw = """
            [ServerSettings]
            ActiveMods=111
        """.trimIndent()

        val parsed = IniSerializer.deserialize<GameUserSettings>(raw).withActiveModsFromRaw(raw)

        assertEquals(listOf(111), parsed.serverSettings.activeMods)
    }

    @Test
    fun `chat and HUD fields round-trip`() {
        val settings = GameUserSettings().copy(
            serverSettings = ServerSettings(
                adminLogging = true,
                globalVoiceChat = true,
                proximityChat = true,
                serverCrosshair = false,
                showMapPlayerLocation = false,
                allowThirdPersonPlayer = false,
                allowHitMarkers = false,
                allowHideDamageSourceFromLogs = false,
                showFloatingDamageText = true,
                serverForceNoHud = true,
                disablePvEGamma = true,
                enablePvPGamma = true,
                preventSpawnAnimations = true,
                dontAlwaysNotifyPlayerJoined = true,
                badWordListUrl = "https://example.com/bad.txt",
                badWordWhiteListUrl = "https://example.com/good.txt",
            )
        )

        val parsed = IniSerializer.deserialize<GameUserSettings>(IniSerializer.serialize(settings))

        assertEquals(settings.serverSettings, parsed.serverSettings)

        val mode = ShooterGameMode(disablePhotoMode = true, photoModeRangeLimit = 5000)
        val parsedGame = IniSerializer.deserialize<Game>(IniSerializer.serialize(Game(shooterGameMode = mode)))

        assertEquals(mode, parsedGame.shooterGameMode)
    }

    @Test
    fun `chat and HUD defaults match ASA`() {
        val settings = ServerSettings()

        assertTrue(settings.serverCrosshair)
        assertTrue(settings.showMapPlayerLocation)
        assertTrue(settings.allowThirdPersonPlayer)
        assertTrue(settings.allowHitMarkers)
        assertTrue(settings.allowHideDamageSourceFromLogs)
        assertFalse(settings.serverForceNoHud)
        assertFalse(settings.showFloatingDamageText)
        assertEquals(3000, ShooterGameMode().photoModeRangeLimit)
    }

    @Test
    fun `structures and items fields round-trip`() {
        val settings = GameUserSettings().copy(
            serverSettings = ServerSettings(
                structureResistanceMultiplier = 2.0f,
                structurePreventResourceRadiusMultiplier = 0.5f,
                theMaxStructuresInRange = 5000,
                ignoreLimitMaxStructuresInRangeTypeFlag = true,
                perPlatformMaxStructuresMultiplier = 3.0f,
                platformSaddleBuildAreaBoundsMultiplier = 2.0f,
                overrideStructurePlatformPrevention = true,
                alwaysAllowStructurePickup = true,
                structurePickupTimeAfterPlacement = 10.0f,
                structurePickupHoldDuration = 1.0f,
                disableStructureDecayPvE = true,
                pvEStructureDecayPeriodMultiplier = 2.0f,
                pvPStructureDecay = true,
                autoDestroyOldStructuresMultiplier = 1.0f,
                itemStackSizeMultiplier = 5.0f,
                clampItemStats = true,
                maxPlatformSaddleStructureLimit = 100,
            )
        )

        val parsed = IniSerializer.deserialize<GameUserSettings>(IniSerializer.serialize(settings))

        assertEquals(settings.serverSettings, parsed.serverSettings)

        val mode = ShooterGameMode(
            disableWirelessCrafting = true,
            disableWirelessCraftingForDinos = true,
            disableWirelessCraftingForPlayers = true,
            disableWirelessCraftingForStructures = true,
            wirelessCraftingRangeOverride = 5000,
            limitGeneratorsNum = 5,
            limitGeneratorsRange = 20000,
            hardLimitTurretsInRange = true,
            limitTurretsInRange = false,
            limitTurretsNum = 50,
            limitTurretsRange = 8000.0f,
        )

        val parsedGame = IniSerializer.deserialize<Game>(IniSerializer.serialize(Game(shooterGameMode = mode)))

        assertEquals(mode, parsedGame.shooterGameMode)
    }

    @Test
    fun `structures and items defaults match ASA`() {
        val settings = ServerSettings()

        assertEquals(10500, settings.theMaxStructuresInRange)
        assertEquals(1.0f, settings.itemStackSizeMultiplier)
        assertEquals(3000, ShooterGameMode().wirelessCraftingRangeOverride)
        assertTrue(ShooterGameMode().limitTurretsInRange)
        assertEquals(100, ShooterGameMode().limitTurretsNum)
    }

    @Test
    fun `admin and security fields round-trip`() {
        val settings = GameUserSettings().copy(
            serverSettings = ServerSettings(
                adminListUrl = "https://example.com/admins.txt",
                cosmeticWhitelistOverride = "https://example.com/cosmetics.txt",
                useCharacterTracker = true,
                updateAllowedCheatersInterval = 120.0f,
                serverEnableMeshChecking = true,
                enableMeshBitingProtection = false,
            )
        )

        val parsed = IniSerializer.deserialize<GameUserSettings>(IniSerializer.serialize(settings))

        assertEquals(settings.serverSettings, parsed.serverSettings)
        assertEquals(600.0f, ServerSettings().updateAllowedCheatersInterval)
        assertTrue(ServerSettings().enableMeshBitingProtection)
    }

    @Test
    fun `url validation requires an http or https host`() {
        assertTrue("https://cdn2.arkdedicated.com/asa/BanList.txt".isValidUrl())
        assertTrue("http://example.com/path".isValidUrl())
        assertFalse("not-a-url".isValidUrl())
        assertFalse("file:///c:/temp/x".isValidUrl())
        assertFalse("https://".isValidUrl())
    }

    @Test
    fun `MultiHome round-trips in SessionSettings`() {
        val settings = GameUserSettings().copy(
            sessionSettings = SessionSettings("MultiHome Server", "192.168.1.20")
        )

        val parsed = IniSerializer.deserialize<GameUserSettings>(IniSerializer.serialize(settings))

        assertEquals(settings.sessionSettings, parsed.sessionSettings)
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
