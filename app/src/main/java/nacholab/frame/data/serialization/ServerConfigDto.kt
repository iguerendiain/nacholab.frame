package nacholab.frame.data.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.domain.model.ServerConfigMainUI

private val ServerConfigJson = Json { ignoreUnknownKeys = true }

/**
 * Single source of truth for [ServerConfig]'s wire/storage representation, shared by local
 * persistence ([nacholab.frame.server.data.repository.ServerConfigRepositorySP]) and network transport
 * (the fullClient -> server socket exchange), so both always agree on the same JSON shape.
 */
fun ServerConfig.toJson(): String = ServerConfigJson.encodeToString(toDto())

/** @throws kotlinx.serialization.SerializationException if [this] isn't a valid [ServerConfigDto]. */
fun String.toServerConfig(): ServerConfig = ServerConfigJson.decodeFromString<ServerConfigDto>(this).toDomain()

@Serializable
data class ServerConfigDto(
    val decorations: List<ServerConfigDecorationDto>,
    val mainUI: ServerConfigMainUIDto,
    val mediaItemTime: Int,
    val reshuffleAfterPlaylistFinish: Boolean,
    val sleepTimerFrom: Int,
    val sleepTimerTo: Int,
    val imageScaling: ServerConfigScalingDto,
    val videoScaling: ServerConfigScalingDto,
    val sortType: ServerConfigSortingDto,
    val dirSortType: ServerConfigSortingDto
)

@Serializable
enum class ServerConfigScalingDto { CROP, FIT }

@Serializable
enum class ServerConfigSortingDto { RANDOM, DATE, NAME, IGNORE }

@Serializable
data class ServerConfigMainUIDto(
    val hideType: ServerConfigMainUIHideTypeDto,
    val hideTimeout: Int
)

@Serializable
enum class ServerConfigMainUIHideTypeDto { TIMEOUT, PAGE_CHANGE, MANUAL, DISABLED }

@Serializable
enum class ServerConfigDecorationPositionDto { TS, TC, TE, MS, MC, ME, BS, BC, BE }

@Serializable
sealed class ServerConfigDecorationDto {
    abstract val position: ServerConfigDecorationPositionDto
    abstract val timeout: Int

    @Serializable
    @SerialName("currentDateTime")
    data class CurrentDateTime(
        override val position: ServerConfigDecorationPositionDto,
        override val timeout: Int,
        val ampm: Boolean,
        val showDate: Boolean,
        val timeFormat: String,
        val dateFormat: String
    ) : ServerConfigDecorationDto()

    @Serializable
    @SerialName("message")
    data class Message(
        override val position: ServerConfigDecorationPositionDto,
        override val timeout: Int,
        val message: String
    ) : ServerConfigDecorationDto()

    @Serializable
    @SerialName("mediaInfo")
    data class MediaInfo(
        override val position: ServerConfigDecorationPositionDto,
        override val timeout: Int,
        val timeFormat: String,
        val dateFormat: String
    ) : ServerConfigDecorationDto()
}

fun ServerConfig.toDto(): ServerConfigDto = ServerConfigDto(
    decorations = decorations.map { it.toDto() },
    mainUI = mainUI.toDto(),
    mediaItemTime = mediaItemTime,
    reshuffleAfterPlaylistFinish = reshuffleAfterPlaylistFinish,
    sleepTimerFrom = sleepTimerFrom,
    sleepTimerTo = sleepTimerTo,
    imageScaling = imageScaling.toDto(),
    videoScaling = videoScaling.toDto(),
    sortType = sortType.toDto(),
    dirSortType = dirSortType.toDto()
)

fun ServerConfigDto.toDomain(): ServerConfig = ServerConfig(
    decorations = decorations.map { it.toDomain() },
    mainUI = mainUI.toDomain(),
    mediaItemTime = mediaItemTime,
    reshuffleAfterPlaylistFinish = reshuffleAfterPlaylistFinish,
    sleepTimerFrom = sleepTimerFrom,
    sleepTimerTo = sleepTimerTo,
    imageScaling = imageScaling.toDomain(),
    videoScaling = videoScaling.toDomain(),
    sortType = sortType.toDomain(),
    dirSortType = dirSortType.toDomain()
)

private fun ServerConfig.ServerConfigScaling.toDto(): ServerConfigScalingDto = when (this) {
    ServerConfig.ServerConfigScaling.CROP -> ServerConfigScalingDto.CROP
    ServerConfig.ServerConfigScaling.FIT -> ServerConfigScalingDto.FIT
}

private fun ServerConfigScalingDto.toDomain(): ServerConfig.ServerConfigScaling = when (this) {
    ServerConfigScalingDto.CROP -> ServerConfig.ServerConfigScaling.CROP
    ServerConfigScalingDto.FIT -> ServerConfig.ServerConfigScaling.FIT
}

private fun ServerConfig.ServerConfigSorting.toDto(): ServerConfigSortingDto = when (this) {
    ServerConfig.ServerConfigSorting.RANDOM -> ServerConfigSortingDto.RANDOM
    ServerConfig.ServerConfigSorting.DATE -> ServerConfigSortingDto.DATE
    ServerConfig.ServerConfigSorting.NAME -> ServerConfigSortingDto.NAME
    ServerConfig.ServerConfigSorting.IGNORE -> ServerConfigSortingDto.IGNORE
}

private fun ServerConfigSortingDto.toDomain(): ServerConfig.ServerConfigSorting = when (this) {
    ServerConfigSortingDto.RANDOM -> ServerConfig.ServerConfigSorting.RANDOM
    ServerConfigSortingDto.DATE -> ServerConfig.ServerConfigSorting.DATE
    ServerConfigSortingDto.NAME -> ServerConfig.ServerConfigSorting.NAME
    ServerConfigSortingDto.IGNORE -> ServerConfig.ServerConfigSorting.IGNORE
}

private fun ServerConfigMainUI.toDto(): ServerConfigMainUIDto = ServerConfigMainUIDto(
    hideType = hideType.toDto(),
    hideTimeout = hideTimeout
)

private fun ServerConfigMainUIDto.toDomain(): ServerConfigMainUI = ServerConfigMainUI(
    hideType = hideType.toDomain(),
    hideTimeout = hideTimeout
)

private fun ServerConfigMainUI.ServerConfigMainUIHideType.toDto(): ServerConfigMainUIHideTypeDto = when (this) {
    ServerConfigMainUI.ServerConfigMainUIHideType.TIMEOUT -> ServerConfigMainUIHideTypeDto.TIMEOUT
    ServerConfigMainUI.ServerConfigMainUIHideType.PAGE_CHANGE -> ServerConfigMainUIHideTypeDto.PAGE_CHANGE
    ServerConfigMainUI.ServerConfigMainUIHideType.MANUAL -> ServerConfigMainUIHideTypeDto.MANUAL
    ServerConfigMainUI.ServerConfigMainUIHideType.DISABLED -> ServerConfigMainUIHideTypeDto.DISABLED
}

private fun ServerConfigMainUIHideTypeDto.toDomain(): ServerConfigMainUI.ServerConfigMainUIHideType = when (this) {
    ServerConfigMainUIHideTypeDto.TIMEOUT -> ServerConfigMainUI.ServerConfigMainUIHideType.TIMEOUT
    ServerConfigMainUIHideTypeDto.PAGE_CHANGE -> ServerConfigMainUI.ServerConfigMainUIHideType.PAGE_CHANGE
    ServerConfigMainUIHideTypeDto.MANUAL -> ServerConfigMainUI.ServerConfigMainUIHideType.MANUAL
    ServerConfigMainUIHideTypeDto.DISABLED -> ServerConfigMainUI.ServerConfigMainUIHideType.DISABLED
}

private fun ServerConfigDecoration.ServerConfigDecorationPosition.toDto(): ServerConfigDecorationPositionDto =
    when (this) {
        ServerConfigDecoration.ServerConfigDecorationPosition.TS -> ServerConfigDecorationPositionDto.TS
        ServerConfigDecoration.ServerConfigDecorationPosition.TC -> ServerConfigDecorationPositionDto.TC
        ServerConfigDecoration.ServerConfigDecorationPosition.TE -> ServerConfigDecorationPositionDto.TE
        ServerConfigDecoration.ServerConfigDecorationPosition.MS -> ServerConfigDecorationPositionDto.MS
        ServerConfigDecoration.ServerConfigDecorationPosition.MC -> ServerConfigDecorationPositionDto.MC
        ServerConfigDecoration.ServerConfigDecorationPosition.ME -> ServerConfigDecorationPositionDto.ME
        ServerConfigDecoration.ServerConfigDecorationPosition.BS -> ServerConfigDecorationPositionDto.BS
        ServerConfigDecoration.ServerConfigDecorationPosition.BC -> ServerConfigDecorationPositionDto.BC
        ServerConfigDecoration.ServerConfigDecorationPosition.BE -> ServerConfigDecorationPositionDto.BE
    }

private fun ServerConfigDecorationPositionDto.toDomain(): ServerConfigDecoration.ServerConfigDecorationPosition =
    when (this) {
        ServerConfigDecorationPositionDto.TS -> ServerConfigDecoration.ServerConfigDecorationPosition.TS
        ServerConfigDecorationPositionDto.TC -> ServerConfigDecoration.ServerConfigDecorationPosition.TC
        ServerConfigDecorationPositionDto.TE -> ServerConfigDecoration.ServerConfigDecorationPosition.TE
        ServerConfigDecorationPositionDto.MS -> ServerConfigDecoration.ServerConfigDecorationPosition.MS
        ServerConfigDecorationPositionDto.MC -> ServerConfigDecoration.ServerConfigDecorationPosition.MC
        ServerConfigDecorationPositionDto.ME -> ServerConfigDecoration.ServerConfigDecorationPosition.ME
        ServerConfigDecorationPositionDto.BS -> ServerConfigDecoration.ServerConfigDecorationPosition.BS
        ServerConfigDecorationPositionDto.BC -> ServerConfigDecoration.ServerConfigDecorationPosition.BC
        ServerConfigDecorationPositionDto.BE -> ServerConfigDecoration.ServerConfigDecorationPosition.BE
    }

private fun ServerConfigDecoration.toDto(): ServerConfigDecorationDto = when (this) {
    is ServerConfigDecoration.CurrentDateTime -> ServerConfigDecorationDto.CurrentDateTime(
        position = position.toDto(),
        timeout = timeout,
        ampm = ampm,
        showDate = showDate,
        timeFormat = timeFormat,
        dateFormat = dateFormat
    )

    is ServerConfigDecoration.Message -> ServerConfigDecorationDto.Message(
        position = position.toDto(),
        timeout = timeout,
        message = message
    )

    is ServerConfigDecoration.MediaInfo -> ServerConfigDecorationDto.MediaInfo(
        position = position.toDto(),
        timeout = timeout,
        timeFormat = timeFormat,
        dateFormat = dateFormat
    )
}

private fun ServerConfigDecorationDto.toDomain(): ServerConfigDecoration = when (this) {
    is ServerConfigDecorationDto.CurrentDateTime -> ServerConfigDecoration.CurrentDateTime(
        position = position.toDomain(),
        timeout = timeout,
        ampm = ampm,
        showDate = showDate,
        timeFormat = timeFormat,
        dateFormat = dateFormat
    )

    is ServerConfigDecorationDto.Message -> ServerConfigDecoration.Message(
        position = position.toDomain(),
        timeout = timeout,
        message = message
    )

    is ServerConfigDecorationDto.MediaInfo -> ServerConfigDecoration.MediaInfo(
        position = position.toDomain(),
        timeout = timeout,
        timeFormat = timeFormat,
        dateFormat = dateFormat
    )
}
