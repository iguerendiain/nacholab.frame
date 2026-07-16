package nacholab.frame.fullclient.domain.usecase

import nacholab.frame.fullclient.domain.repository.ConnectionConfigRepository
import javax.inject.Inject

class HasConnectionConfigUseCase @Inject constructor(
    private val repository: ConnectionConfigRepository
) {
    operator fun invoke() = repository.getConnectionConfig() != null
}
