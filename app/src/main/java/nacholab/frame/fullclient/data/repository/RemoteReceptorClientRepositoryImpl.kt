package nacholab.frame.fullclient.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nacholab.frame.data.model.toJson
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.fullclient.domain.repository.RemoteReceptorClientRepository
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class RemoteReceptorClientRepositoryImpl @Inject constructor() : RemoteReceptorClientRepository {

    companion object {
        private const val CONNECT_TIMEOUT_MILLIS = 5_000
    }

    override suspend fun sendServerConfig(config: ServerConfig, host: String, port: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(host, port), CONNECT_TIMEOUT_MILLIS)

                    socket.getOutputStream().bufferedWriter().use { writer ->
                        writer.write(config.toJson())
                        writer.newLine()
                        writer.flush()
                    }
                }
            }
        }
}
