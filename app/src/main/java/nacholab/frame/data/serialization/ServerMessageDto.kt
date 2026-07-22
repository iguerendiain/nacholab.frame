package nacholab.frame.data.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import nacholab.frame.domain.model.ServerMessage

private val ServerMessageJson = Json { ignoreUnknownKeys = true }

/**
 * Single source of truth for [ServerMessage]'s wire representation, shared by both ends of the
 * fullClient <-> server socket exchange, so both always agree on the same JSON shape.
 */
fun <T> ServerMessage<T>.toJson(payloadSerializer: KSerializer<T>): String =
    ServerMessageJson.encodeToString(ServerMessageDto.serializer(payloadSerializer), toDto())

/** @throws kotlinx.serialization.SerializationException if [this] isn't a valid [ServerMessageDto]. */
fun <T> String.toServerMessage(payloadSerializer: KSerializer<T>): ServerMessage<T> =
    ServerMessageJson.decodeFromString(ServerMessageDto.serializer(payloadSerializer), this).toDomain()

@Serializable
data class ServerMessageDto<T>(
    val type: ServerMessageTypeDto,
    val payload: T?
)

@Serializable
enum class ServerMessageTypeDto { SEND_CONFIG, GET_CONFIG, GET_CONFIG_RESPONSE, RELOAD_PLAYLIST }

fun <T> ServerMessage<T>.toDto(): ServerMessageDto<T> = ServerMessageDto(
    type = type.toDto(),
    payload = payload
)

fun <T> ServerMessageDto<T>.toDomain(): ServerMessage<T> = ServerMessage(
    type = type.toDomain(),
    payload = payload
)

private fun ServerMessage.MessageType.toDto(): ServerMessageTypeDto = when (this) {
    ServerMessage.MessageType.SEND_CONFIG -> ServerMessageTypeDto.SEND_CONFIG
    ServerMessage.MessageType.GET_CONFIG -> ServerMessageTypeDto.GET_CONFIG
    ServerMessage.MessageType.GET_CONFIG_RESPONSE -> ServerMessageTypeDto.GET_CONFIG_RESPONSE
    ServerMessage.MessageType.RELOAD_PLAYLIST -> ServerMessageTypeDto.RELOAD_PLAYLIST
}

private fun ServerMessageTypeDto.toDomain(): ServerMessage.MessageType = when (this) {
    ServerMessageTypeDto.SEND_CONFIG -> ServerMessage.MessageType.SEND_CONFIG
    ServerMessageTypeDto.GET_CONFIG -> ServerMessage.MessageType.GET_CONFIG
    ServerMessageTypeDto.GET_CONFIG_RESPONSE -> ServerMessage.MessageType.GET_CONFIG_RESPONSE
    ServerMessageTypeDto.RELOAD_PLAYLIST -> ServerMessage.MessageType.RELOAD_PLAYLIST
}
