package nacholab.frame.fullclient.domain.usecase

import nacholab.frame.fullclient.domain.model.ConnectionConfig
import nacholab.frame.fullclient.domain.repository.ConnectionConfigRepository
import javax.inject.Inject

class SaveConnectionConfigUseCase @Inject constructor(
    private val repository: ConnectionConfigRepository
) {
    operator fun invoke(config: ConnectionConfig) = repository.saveConnectionConfig(config)
}
