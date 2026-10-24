package eu.wynq.arkascendedservermanager.core.server

import eu.wynq.arkascendedservermanager.core.db.models.Server
import eu.wynq.arkascendedservermanager.core.ini.Game
import eu.wynq.arkascendedservermanager.core.ini.GameUserSettings
import eu.wynq.arkascendedservermanager.core.ini.ServerSettings
import eu.wynq.arkascendedservermanager.core.ini.SessionSettings
import eu.wynq.arkascendedservermanager.core.managers.InstallManager
import eu.wynq.arkascendedservermanager.core.support.Constants
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ServerIoTest {

    private lateinit var root: Path

    @BeforeTest
    fun setUp() {
        root = Files.createTempDirectory("aasm-server-io")
    }

    @AfterTest
    fun tearDown() {
        root.toFile().deleteRecursively()
    }

    private fun server(asaApi: Boolean = false): Server = Server(
        id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
        profileName = "IO Test Server",
        installationLocation = root.toAbsolutePath().toString(),
        asaApi = asaApi,
        settings = Settings(
            administration = Administration(
                map = "TheIsland_WP",
                serverPort = 7780,
                queryPort = 27015,
                rconEnabled = true,
                rconPort = 27020,
                serverPassword = "secret",
                adminPassword = "Admin123!",
                slots = 42,
                mods = listOf("111"),
            )
        ),
        gameUserSettings = GameUserSettings().copy(
            sessionSettings = SessionSettings("IO Server"),
            serverSettings = ServerSettings(
                activeMods = listOf(111, 222),
                tamingSpeedMultiplier = 2.0f,
                maxTamedDinos = 1234,
            )
        ),
        game = Game().copy(
            shooterGameMode = Game().shooterGameMode.copy(maxNumberOfPlayersInTribe = 5)
        )
    )

    @Test
    fun `writes and reads both INI files`() {
        val server = server()

        server.saveGusToInstall().getOrThrow()
        server.saveGameToInstall().getOrThrow()

        val gusPath = root.resolve(Constants.GAME_USER_SETTINGS_PATH)
        val gamePath = root.resolve(Constants.GAME_PATH)
        assertTrue(gusPath.exists(), "GameUserSettings.ini should exist")
        assertTrue(gamePath.exists(), "Game.ini should exist")

        val gus = server.getGusFromInstall().getOrThrow()!!
        assertEquals(listOf(111, 222), gus.serverSettings.activeMods)
        assertEquals(2.0f, gus.serverSettings.tamingSpeedMultiplier)
        assertEquals(1234, gus.serverSettings.maxTamedDinos)
        assertEquals("IO Server", gus.sessionSettings.sessionName)

        val game = server.getGameFromInstall().getOrThrow()!!
        assertEquals(5, game.shooterGameMode.maxNumberOfPlayersInTribe)
    }

    @Test
    fun `startup script keeps existing arguments and selects the executable`() {
        val script = server().makeStartupScriptString()
        assertTrue(script.contains("ArkAscendedServer.exe"))
        assertTrue(script.contains("?Port=7780"))
        assertTrue(script.contains("?QueryPort=27015"))
        assertTrue(script.contains("?ServerPassword=secret"))
        assertTrue(script.contains("?ServerAdminPassword=Admin123!"))
        assertTrue(script.contains("-WinLiveMaxPlayers=42"))
        assertTrue(!script.contains("-mods="), "mods are INI-only unless mirroring is enabled")

        assertTrue(server(asaApi = true).makeStartupScriptString().contains("Overseer.exe"))
    }

    @Test
    fun `createStartupScript writes the batch file`() {
        InstallManager.createStartupScript(server()).getOrThrow()

        val path = root.resolve(Constants.STARTUP_SCRIPT_PATH)
        assertTrue(path.exists())
        assertTrue(path.toFile().readText().contains("ArkAscendedServer.exe"))
        assertTrue(path.toFile().readText().contains("?Port=7780"))
    }

    @Test
    fun `isInstalled checks for the server executable`() {
        assertFalse(InstallManager.isInstalled(root))

        val executable = root.resolve(Constants.SERVER_BINARY_PATH).resolve(Constants.SERVER_EXECUTABLE_NAME)
        Files.createDirectories(executable.parent)
        Files.writeString(executable, "")

        assertTrue(InstallManager.isInstalled(root))
    }

    @Test
    fun `getServerVersion reads version txt before touching the executable`() {
        val versionFile = root.resolve(Constants.SERVER_VERSION_PATH)
        Files.createDirectories(versionFile.parent)
        Files.writeString(versionFile, "1.2.3")

        assertEquals("1.2.3", InstallManager.getServerVersion(server()))
    }

    @Test
    fun `unknown keys survive a load and save cycle`() {
        val path = root.resolve(Constants.GAME_USER_SETTINGS_PATH)
        Files.createDirectories(path.parent)
        Files.writeString(
            path,
            """
            [ServerSettings]
            ActiveMods=111
            SomeFutureModKey=keep-me
            """.trimIndent()
        )

        val loaded = server().getGusFromInstall().getOrThrow()!!
        server().copy(gameUserSettings = loaded).saveGusToInstall().getOrThrow()

        val saved = path.toFile().readText()
        assertTrue(saved.contains("SomeFutureModKey=keep-me"))
        assertTrue(saved.contains("ActiveMods=111"))
    }

    @Test
    fun `validate accepts a complete profile and rejects a blank name`() {
        assertTrue(server().validate())
        assertFalse(server().copy(profileName = "").validate())
    }
}
