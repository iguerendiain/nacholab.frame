package nacholab.frame.fullclient.domain.repository

import nacholab.frame.fullclient.domain.model.ConnectionConfig

interface ConnectionConfigRepository {

    fun getConnectionConfig(): ConnectionConfig?

    fun saveConnectionConfig(config: ConnectionConfig)

    fun clearConnectionConfig()

}
