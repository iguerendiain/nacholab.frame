package nacholab.frame.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

class RemoteReceptorServer{
    private var serverSocket: ServerSocket? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var callback: ((RemoteReceptorCommand) -> Unit)? = null

    data class RemoteReceptorCommand(
        val order: String
    )

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
            val order = reader.readLine()
//            processOrder(order)
        }
    }

    fun stopServer() {
        scope.cancel()
        serverSocket?.close()
    }
}