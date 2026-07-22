package nacholab.frame.fullclient.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import nacholab.frame.fullclient.domain.model.ConnectionConfig
import nacholab.frame.fullclient.domain.repository.ConnectionConfigRepository
import javax.inject.Inject

class ConnectionConfigRepositorySP @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ConnectionConfigRepository {

    companion object {
        const val KEY_HOST = "fullClientHost"
        const val KEY_PORT = "fullClientPort"
    }

    override fun getConnectionConfig(): ConnectionConfig? {
        val host = sharedPreferences.getString(KEY_HOST, null) ?: return null
        val port = sharedPreferences.getInt(KEY_PORT, -1)
        if (port == -1) return null

        return ConnectionConfig(host = host, port = port)
    }

    override fun saveConnectionConfig(config: ConnectionConfig) {
        sharedPreferences.edit {
            putString(KEY_HOST, config.host)
            putInt(KEY_PORT, config.port)
        }
    }

    override fun clearConnectionConfig() {
        sharedPreferences.edit {
            remove(KEY_HOST)
            remove(KEY_PORT)
        }
    }
}
