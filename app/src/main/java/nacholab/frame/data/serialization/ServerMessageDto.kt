package nacholab.frame.data.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import nacholab.frame.domain.model.ServerMessage

private val ServerMessageJson = Json { ignoreUnknownKeys = true }

/**
 * Single source of truth for [ServerMessage]'s wire representation, shared by both ends of the
 * fullClient <-> server socket exchange, so both always agree on the same JSON shape. The `type`
 * discriminator (kotlinx.serialization's default sealed-class key) is what determines which
 * concrete payload shape follows, mirroring [ServerConfigDecorationDto].
 */
fun ServerMessage.toJson(): String = ServerMessageJson.encodeToString(ServerMessageDto.serializer(), toDto())

/** @throws kotlinx.serialization.SerializationException if [this] isn't a valid [ServerMessageDto]. */
fun String.toServerMessage(): ServerMessage =
    ServerMessageJson.decodeFromString(ServerMessageDto.serializer(), this).toDomain()

@Serializable
sealed class ServerMessageDto {

    @Serializable
    @SerialName("SEND_CONFIG")
    data class SendConfig(val payload: ServerConfigDto) : ServerMessageDto()

    @Serializable
    @SerialName("GET_CONFIG")
    data class GetConfig(val payload: ServerConfigDto) : ServerMessageDto()

    @Serializable
    @SerialName("GET_CONFIG_RESPONSE")
    data class GetConfigResponse(val payload: ServerConfigDto) : ServerMessageDto()

    @Serializable
    @SerialName("RELOAD_PLAYLIST")
    data object ReloadPlaylist : ServerMessageDto()

}

fun ServerMessage.toDto(): ServerMessageDto = when (this) {
    is ServerMessage.SendConfig -> ServerMessageDto.SendConfig(payload.toDto())
    is ServerMessage.GetConfig -> ServerMessageDto.GetConfig(payload.toDto())
    is ServerMessage.GetConfigResponse -> ServerMessageDto.GetConfigResponse(payload.toDto())
    ServerMessage.ReloadPlaylist -> ServerMessageDto.ReloadPlaylist
}

fun ServerMessageDto.toDomain(): ServerMessage = when (this) {
    is ServerMessageDto.SendConfig -> ServerMessage.SendConfig(payload.toDomain())
    is ServerMessageDto.GetConfig -> ServerMessage.GetConfig(payload.toDomain())
    is ServerMessageDto.GetConfigResponse -> ServerMessage.GetConfigResponse(payload.toDomain())
    ServerMessageDto.ReloadPlaylist -> ServerMessage.ReloadPlaylist
}
