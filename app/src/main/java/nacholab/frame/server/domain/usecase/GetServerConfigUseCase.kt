package nacholab.frame.server.domain.usecase

import nacholab.frame.server.domain.repository.ServerConfigRepository
import javax.inject.Inject

class GetServerConfigUseCase @Inject constructor(
    private val repository: ServerConfigRepository
) {
    operator fun invoke() = repository.getServerConfig()
}
