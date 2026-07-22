package nacholab.frame.domain.usecase

import nacholab.frame.domain.repository.ServerConfigRepository
import javax.inject.Inject

class ClearServerConfigUseCase @Inject constructor(
    private val repository: ServerConfigRepository
) {
    operator fun invoke() = repository.clearServerConfig()
}
