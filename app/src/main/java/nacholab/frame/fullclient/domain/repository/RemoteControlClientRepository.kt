package nacholab.frame.fullclient.domain.repository

import nacholab.frame.domain.model.ServerConfig

interface RemoteControlClientRepository {

    suspend fun sendServerConfig(config: ServerConfig, host: String, port: Int): Result<Unit>

}
