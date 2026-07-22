package nacholab.frame.domain.model

data class ServerMessage<T>(
    val type: MessageType,
    val payload: T?
){
    enum class MessageType {
        SEND_CONFIG,
        GET_CONFIG,
        GET_CONFIG_RESPONSE,
        RELOAD_PLAYLIST
    }

}