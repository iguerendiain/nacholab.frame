package nacholab.frame.fullclient.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nacholab.frame.data.serialization.toJson
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerMessage
import nacholab.frame.fullclient.domain.repository.RemoteControlClientRepository
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class RemoteControlClientRepositorySocket @Inject constructor() : RemoteControlClientRepository {

    companion object {
        private const val CONNECT_TIMEOUT_MILLIS = 5_000
    }

    private suspend fun sendThroughSocket(host: String, port: Int, data: String) =
        withContext(Dispatchers.IO) {
            runCatching {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(host, port), CONNECT_TIMEOUT_MILLIS)

                    socket.getOutputStream().bufferedWriter().use { writer ->
                        writer.write(data)
                        writer.newLine()
                        writer.flush()
                    }
                }
            }
        }

    private suspend fun sendThroughSocket(host: String, port: Int, data: ServerMessage) =
        sendThroughSocket(host, port, data.toJson())

    override suspend fun sendServerConfig(config: ServerConfig, host: String, port: Int): Result<Unit> =
        sendThroughSocket(host, port, ServerMessage.SendConfig(config))
}
