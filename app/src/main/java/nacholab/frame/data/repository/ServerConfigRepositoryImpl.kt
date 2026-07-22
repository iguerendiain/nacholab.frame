package nacholab.frame.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.SerializationException
import nacholab.frame.data.model.toJson
import nacholab.frame.data.model.toServerConfig
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.repository.ServerConfigRepository
import javax.inject.Inject

class ServerConfigRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ServerConfigRepository {

    companion object {
        private const val KEY_SERVER_CONFIG = "serverConfig"
    }

    override fun getServerConfig(): ServerConfig? {
        val serialized = sharedPreferences.getString(KEY_SERVER_CONFIG, null) ?: return null

        return try {
            serialized.toServerConfig()
        } catch (e: SerializationException) {
            null
        }
    }

    override fun saveServerConfig(config: ServerConfig) {
        sharedPreferences.edit {
            putString(KEY_SERVER_CONFIG, config.toJson())
        }
    }

    override fun clearServerConfig() {
        sharedPreferences.edit {
            remove(KEY_SERVER_CONFIG)
        }
    }
}
