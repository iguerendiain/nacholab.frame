package nacholab.frame.domain.model

sealed class ServerMessage {

    data class SendConfig(val payload: ServerConfig) : ServerMessage()

    data class GetConfig(val payload: ServerConfig) : ServerMessage()

    data class GetConfigResponse(val payload: ServerConfig) : ServerMessage()

    data object ReloadPlaylist : ServerMessage()

}