@file:OptIn(kotlin.uuid.ExperimentalUuidApi::class)

package eu.wynq.arkascendedservermanager.core.server

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.uuid.Uuid

class SettingsStartupScriptTest {

    @Test
    fun `roundtrips generated startup script`() {
        val settings = Settings(
            administration = Administration(
                map = "TheIsland_WP",
                serverPort = 7780,
                queryPort = 27015,
                rconEnabled = false,
                rconPort = 27016,
                serverPassword = null,
                adminPassword = "Admin123!",
                slots = 96,
                mods = listOf("12345", "67890")
            ),
            options = Options(enableIdlePlayerKick = true)
        )

        val server = Server(
            id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
            profileName = "Test Server",
            installationLocation = "C:\\Ark",
            asaApi = false,
            settings = settings,
            gameUserSettings = GameUserSettings()
        )

        val script = server.makeStartupScriptString()
        val parsed = Settings.fromStartupScriptString(script)

        assertTrue(script.contains("?Port=7780"))
        assertEquals(settings.administration.serverPort, parsed.administration.serverPort)
        assertEquals(settings.administration.queryPort, parsed.administration.queryPort)
        assertEquals(settings.administration.rconEnabled, parsed.administration.rconEnabled)
        assertEquals(settings.administration.adminPassword, parsed.administration.adminPassword)
        assertEquals(settings.administration.mods, parsed.administration.mods)
        assertEquals(settings.options, parsed.options)
        assertTrue(script.contains("?RCONEnabled=False"))
    }

    @Test
    fun `omits null query values and keeps forced flags`() {
        val settings = Settings(
            administration = Administration(
                serverPassword = null,
                adminPassword = "Admin123!"
            ),
            options = Options(
                enableIdlePlayerKick = false,
                oldConsole = false,
                noGameAnalytics = false
            )
        )

        val args = settings.toStartupScriptArguments()

        assertTrue(args.any { it.contains("?Port=7777") })
        assertTrue(args.contains("-OldConsole"))
        assertTrue(args.contains("-NoGameAnalytics"))
        assertTrue(args.none { it.startsWith("?ServerPassword=") })
    }

    @Test
    fun `blank boolean query falls back to default and does not crash`() {
        val script = """start C:\\Ark\\Server.exe TheIsland_WP?ServerAdminPassword=Admin123!?Port=7780?QueryPort=27015?RCONEnabled= -OldConsole -NoGameAnalytics"""

        val parsed = Settings.fromStartupScriptString(script)

        assertTrue(parsed.administration.rconEnabled)
    }

    @Test
    fun `malformed boolean query falls back to default and does not crash`() {
        val script = """start C:\\Ark\\Server.exe TheIsland_WP?ServerAdminPassword=Admin123!?Port=7780?QueryPort=27015?RCONEnabled=maybe -OldConsole -NoGameAnalytics"""

        val parsed = Settings.fromStartupScriptString(script)

        assertTrue(parsed.administration.rconEnabled)
    }

    @Test
    fun `parses reordered and hand edited startup scripts`() {
        val script = """start C:\\Ark\\Server.exe Fjordur?ServerAdminPassword=Admin123!?QueryPort=27015?Port=7780?RCONEnabled=False?ServerPassword=secret -mods=111,222 -WinLiveMaxPlayers=80 -EnableIdlePlayerKick -OldConsole -NoGameAnalytics"""

        val parsed = Settings.fromStartupScriptString(script)

        assertEquals("Fjordur", parsed.administration.map)
        assertEquals(7780, parsed.administration.serverPort)
        assertEquals(27015, parsed.administration.queryPort)
        assertEquals(false, parsed.administration.rconEnabled)
        assertEquals("secret", parsed.administration.serverPassword)
        assertEquals("Admin123!", parsed.administration.adminPassword)
        assertEquals(80, parsed.administration.slots)
        assertEquals(listOf("111", "222"), parsed.administration.mods)
        assertTrue(parsed.options.enableIdlePlayerKick)
    }

    @Test
    fun `parses empty mods and blank server password`() {
        val script = """start C:\\Ark\\Server.exe TheIsland_WP?ServerAdminPassword=Admin123!?Port=7780?QueryPort=27015?ServerPassword= -mods= -WinLiveMaxPlayers=70 -OldConsole -NoGameAnalytics"""

        val parsed = Settings.fromStartupScriptString(script)

        assertEquals(7780, parsed.administration.serverPort)
        assertEquals(null, parsed.administration.serverPassword)
        assertEquals(emptyList(), parsed.administration.mods)
        assertTrue(parsed.options.oldConsole)
        assertTrue(parsed.options.noGameAnalytics)
    }

    @Test
    fun `infers rcon enabled from rcon port`() {
        val script = """start C:\\Ark\\Server.exe TheIsland_WP?ServerAdminPassword=Admin123!?Port=7780?QueryPort=27015?RCONPort=27016 -OldConsole -NoGameAnalytics"""

        val parsed = Settings.fromStartupScriptString(script)

        assertEquals(true, parsed.administration.rconEnabled)
    }

    @Test
    fun `forces required flags when missing in imported startup script`() {
        val script = """start C:\\Ark\\Server.exe TheIsland_WP?ServerAdminPassword=Admin123!?QueryPort=27015?Port=7780 -WinLiveMaxPlayers=70"""

        val parsed = Settings.fromStartupScriptString(script)

        assertTrue(parsed.options.oldConsole)
        assertTrue(parsed.options.noGameAnalytics)

        val startupArgs = parsed.toStartupScriptArguments()
        assertTrue(startupArgs.contains("-OldConsole"))
        assertTrue(startupArgs.contains("-NoGameAnalytics"))
    }

    @Test
    fun `ignores malformed values and keeps defaults`() {
        val script = """start C:\\Ark\\Server.exe Fjordur?ServerAdminPassword=Admin123!?Port=abc?QueryPort=27015?RCONEnabled=maybe?ServerPassword=?RCONPort=oops -mods=111,foo,,222 -WinLiveMaxPlayers=notanumber -EnableIdlePlayerKick=oops -OldConsole -NoGameAnalytics"""

        val parsed = Settings.fromStartupScriptString(script)

        assertEquals("Fjordur", parsed.administration.map)
        assertEquals(7777, parsed.administration.serverPort)
        assertEquals(27015, parsed.administration.queryPort)
        assertEquals(true, parsed.administration.rconEnabled)
        assertEquals(null, parsed.administration.serverPassword)
        assertEquals(70, parsed.administration.slots)
        assertEquals(listOf("111", "222"), parsed.administration.mods)
        assertTrue(parsed.options.oldConsole)
        assertTrue(parsed.options.noGameAnalytics)
        assertTrue(parsed.options.enableIdlePlayerKick.not())
    }
}

