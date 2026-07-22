package nacholab.frame.fullclient.domain.repository

import nacholab.frame.domain.model.ServerConfig

interface RemoteReceptorClientRepository {

    suspend fun sendServerConfig(config: ServerConfig, host: String, port: Int): Result<Unit>

}
