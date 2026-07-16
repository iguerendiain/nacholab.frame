package nacholab.frame.fullclient.domain.usecase

import javax.inject.Inject

class ValidateHostUseCase @Inject constructor() {

    private val hostnameRegex = Regex(
        "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9])\\.)*" +
            "([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9-]*[A-Za-z0-9])$"
    )

    private val ipv4Regex = Regex(
        "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$"
    )

    private val numericHostRegex = Regex("^[0-9.]+$")

    operator fun invoke(host: String): Boolean {
        if (host.isBlank()) return false

        // A purely numeric/dotted host is a malformed IPv4 attempt, not a hostname
        // (e.g. "192.168.1.500"), so it must not fall through to the lenient hostname check.
        if (numericHostRegex.matches(host)) return ipv4Regex.matches(host)

        return hostnameRegex.matches(host)
    }
}
