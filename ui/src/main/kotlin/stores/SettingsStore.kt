package eu.wynq.arkascendedservermanager.ui.stores

import eu.wynq.arkascendedservermanager.core.db.models.Settings
import eu.wynq.arkascendedservermanager.core.db.repositories.SettingsRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface SettingsStore {
    val settings: kotlinx.coroutines.flow.StateFlow<Settings?>
    val error: kotlinx.coroutines.flow.StateFlow<String?>
    fun loadSettings()
    fun updateSettings(settings: Settings)
}

class SettingsStoreImpl : SettingsStore {
    private val logger = KotlinLogging.logger {}
    private val _settings = MutableStateFlow<Settings?>(null)
    override val settings = _settings.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    override val error = _error.asStateFlow()

    init {
        loadSettings()
    }

    override fun loadSettings() {
        SettingsRepository.getSettings().onSuccess {
            _settings.value = it
            _error.value = null
        }.onFailure {
            logger.error(it) { "Failed to load settings" }
            _error.value = it.message
            _settings.value = null
        }
    }

    override fun updateSettings(settings: Settings) {
        _settings.value = settings
        SettingsRepository.saveSettings(settings).onSuccess {
            _error.value = null
        }.onFailure {
            logger.error(it) { "Failed to save settings" }
            _error.value = it.message
        }
    }
}