package nacholab.frame.fullclient.domain.usecase

import nacholab.frame.fullclient.domain.model.ConnectionConfig
import nacholab.frame.utils.ConnectionUri
import javax.inject.Inject

class ParseConnectionUriUseCase @Inject constructor(
    private val validateHostUseCase: ValidateHostUseCase,
    private val validatePortUseCase: ValidatePortUseCase
) {
    operator fun invoke(uri: String): ConnectionConfig? {
        val (host, port) = ConnectionUri.decode(uri) ?: return null
        if (!validateHostUseCase(host) || !validatePortUseCase(port.toString())) return null

        return ConnectionConfig(host = host, port = port)
    }
}
