package nacholab.frame.domain.model

data class ServerConfig(
    val decorations: List<ServerConfigDecoration>,
    val mainUI: ServerConfigMainUI,
    val mediaItemTime: Int,
    val reshuffleAfterPlaylistFinish: Boolean,
    val sleepTimerFrom: Int,
    val sleepTimerTo: Int,
    val imageScaling: ServerConfigScaling,
    val videoScaling: ServerConfigScaling,
    val sortType: ServerConfigSorting,
    val dirSortType: ServerConfigSorting
){
    enum class ServerConfigScaling { CROP, FIT }
    enum class ServerConfigSorting { RANDOM, DATE, NAME, IGNORE }
}

sealed class ServerConfigDecoration(
    open val position: ServerConfigDecorationPosition,
    open val timeout: Int
){
    enum class ServerConfigDecorationPosition{
        TS, TC, TE,
        MS, MC, ME,
        BS, BC, BE
    }

    data class CurrentDateTime(
        override val position: ServerConfigDecorationPosition,
        override val timeout: Int,
        val ampm: Boolean,
        val showDate: Boolean,
        val timeFormat: String,
        val dateFormat: String
    ): ServerConfigDecoration(
        position = position,
        timeout = timeout
    )

    data class Message(
        override val position: ServerConfigDecorationPosition,
        override val timeout: Int,
        val message: String
    ): ServerConfigDecoration(
        position = position,
        timeout = timeout
    )

    data class MediaInfo(
        override val position: ServerConfigDecorationPosition,
        override val timeout: Int,
        val timeFormat: String,
        val dateFormat: String
    ): ServerConfigDecoration(
        position = position,
        timeout = timeout
    )
}

data class ServerConfigMainUI(
    val hideType: ServerConfigMainUIHideType,
    val hideTimeout: Int,
){
    enum class ServerConfigMainUIHideType {
        TIMEOUT, PAGE_CHANGE, MANUAL, DISABLED
    }
}