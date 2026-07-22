package nacholab.frame.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nacholab.frame.data.model.ServerConfigDto
import nacholab.frame.data.model.toDomain
import nacholab.frame.data.model.toDto
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.repository.ServerConfigRepository
import javax.inject.Inject

class ServerConfigRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ServerConfigRepository {

    companion object {
        private const val KEY_SERVER_CONFIG = "serverConfig"
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun getServerConfig(): ServerConfig? {
        val serialized = sharedPreferences.getString(KEY_SERVER_CONFIG, null) ?: return null

        return try {
            json.decodeFromString<ServerConfigDto>(serialized).toDomain()
        } catch (e: SerializationException) {
            null
        }
    }

    override fun saveServerConfig(config: ServerConfig) {
        sharedPreferences.edit {
            putString(KEY_SERVER_CONFIG, json.encodeToString(config.toDto()))
        }
    }

    override fun clearServerConfig() {
        sharedPreferences.edit {
            remove(KEY_SERVER_CONFIG)
        }
    }
}
