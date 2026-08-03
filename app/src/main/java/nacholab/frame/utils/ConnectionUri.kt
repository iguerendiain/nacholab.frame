package nacholab.frame.utils

import android.net.Uri

/**
 * Single source of truth for the connection pairing URI: it is both encoded into the QR code
 * shown by the server (see RemoteInfo) and decoded by the full client, whether scanned in-app
 * or opened via an external QR/camera app through the "nacholabframe" deep link.
 */
object ConnectionUri {

    private const val SCHEME = "nacholabframe"

    fun encode(host: String, port: Int): String = "$SCHEME://$host:$port"

    fun decode(uri: String): Pair<String, Int>? {
        val parsed = Uri.parse(uri)
        if (parsed.scheme != SCHEME) return null

        val host = parsed.host?.takeIf { it.isNotBlank() } ?: return null
        val port = parsed.port
        if (port !in 1..65535) return null

        return host to port
    }
}
