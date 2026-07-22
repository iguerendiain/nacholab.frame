package nacholab.frame.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import nacholab.frame.data.serialization.toServerConfig
import nacholab.frame.domain.model.ServerConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import javax.inject.Inject

class RemoteControlServer @Inject constructor() {
    private var serverSocket: ServerSocket? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var onServerConfigReceived: ((ServerConfig) -> Unit)? = null

    fun startServer(){
        scope.launch {
            try {
                serverSocket = ServerSocket(47047)

                while (isActive) {
                    val clientSocket = serverSocket?.accept()
                    handleClient(clientSocket)
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun handleClient(socket: Socket?) {
        socket?.use { client ->
            val reader = BufferedReader(InputStreamReader(client.getInputStream()))
            val message = reader.readLine() ?: return

            val config = try {
                message.toServerConfig()
            } catch (e: SerializationException) {
                return
            }

            onServerConfigReceived?.invoke(config)
        }
    }

    fun stopServer() {
        scope.cancel()
        serverSocket?.close()
    }
}