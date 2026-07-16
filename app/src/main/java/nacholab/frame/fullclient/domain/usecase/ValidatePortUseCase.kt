package nacholab.frame.fullclient.domain.usecase

import javax.inject.Inject

class ValidatePortUseCase @Inject constructor() {

    operator fun invoke(port: String): Boolean {
        val portNumber = port.toIntOrNull() ?: return false
        return portNumber in 1..65535
    }
}
