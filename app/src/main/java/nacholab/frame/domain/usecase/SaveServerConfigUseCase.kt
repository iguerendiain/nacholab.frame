package nacholab.frame.domain.usecase

import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.repository.ServerConfigRepository
import javax.inject.Inject

class SaveServerConfigUseCase @Inject constructor(
    private val repository: ServerConfigRepository
) {
    operator fun invoke(config: ServerConfig) = repository.saveServerConfig(config)
}
