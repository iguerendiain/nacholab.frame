package nacholab.frame.domain.repository

import nacholab.frame.domain.model.ServerConfig

interface ServerConfigRepository {

    fun getServerConfig(): ServerConfig?

    fun saveServerConfig(config: ServerConfig)

    fun clearServerConfig()

}
