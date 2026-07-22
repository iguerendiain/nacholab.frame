package nacholab.frame.fullclient.domain.usecase

import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.fullclient.domain.repository.RemoteReceptorClientRepository
import javax.inject.Inject

class SendServerConfigUseCase @Inject constructor(
    private val getConnectionConfigUseCase: GetConnectionConfigUseCase,
    private val remoteReceptorClientRepository: RemoteReceptorClientRepository
) {
    suspend operator fun invoke(config: ServerConfig): Result<Unit> {
        val connectionConfig = getConnectionConfigUseCase()
            ?: return Result.failure(IllegalStateException("No server connection configured"))

        return remoteReceptorClientRepository.sendServerConfig(
            config = config,
            host = connectionConfig.host,
            port = connectionConfig.port
        )
    }
}
