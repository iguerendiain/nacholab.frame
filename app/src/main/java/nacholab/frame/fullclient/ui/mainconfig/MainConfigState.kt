package nacholab.frame.fullclient.ui.mainconfig

import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.domain.model.ServerConfigMainUI

data class MainConfigState(
    val host: String,
    val port: Int,
    val frameName: String,
    val autoSync: Boolean,
    val mediaItemTime: String,
    val mediaItemTimeError: Boolean,
    val reshuffleAfterPlaylistFinish: Boolean,
    val automcaticallyAdvanceMedia: Boolean,
    val sleepTimerFrom: String,
    val sleepTimerFromError: Boolean,
    val sleepTimerTo: String,
    val sleepTimerToError: Boolean,
    val imageScaling: ServerConfig.ServerConfigScaling,
    val videoScaling: ServerConfig.ServerConfigScaling,
    val directorySortEnabled: Boolean,
    val sortType: ServerConfig.ServerConfigSorting,
    val dirSortType: ServerConfig.ServerConfigSorting,
    val mainUIHideType: String,
    val mainUIHideTimeout: ServerConfigMainUI.ServerConfigMainUIHideType,
    val decorations: List<ServerConfigDecoration>,
    val decorationDraft: DecorationDraftState
) {
    companion object {
        val DEFAULT = MainConfigState(
            host = "",
            port = 8047,
            frameName = "",
            autoSync = true,
            mediaItemTime = "10",
            mediaItemTimeError = false,
            reshuffleAfterPlaylistFinish = false,
            automcaticallyAdvanceMedia = true,
            sleepTimerFrom = "",
            sleepTimerFromError = false,
            sleepTimerTo = "",
            sleepTimerToError = false,
            imageScaling = ServerConfig.ServerConfigScaling.CROP,
            videoScaling = ServerConfig.ServerConfigScaling.CROP,
            directorySortEnabled = false,
            sortType = ServerConfig.ServerConfigSorting.RANDOM,
            dirSortType = ServerConfig.ServerConfigSorting.RANDOM,
            mainUIHideType = "",
            mainUIHideTimeout = ServerConfigMainUI.ServerConfigMainUIHideType.DISABLED,
            decorations = emptyList(),
            decorationDraft = DecorationDraftState.DEFAULT
        )
    }
}

data class DecorationDraftState(
    val kind: DecorationKind,
    val position: ServerConfigDecoration.ServerConfigDecorationPosition,
    val timeout: String,
    val timeoutError: Boolean,
    val message: String,
    val messageError: Boolean,
    val ampm: Boolean,
    val showDate: Boolean,
    val timeFormat: String,
    val dateFormat: String
) {
    enum class DecorationKind { CURRENT_DATE_TIME, MESSAGE, MEDIA_INFO }

    companion object {
        val DEFAULT = DecorationDraftState(
            kind = DecorationKind.MESSAGE,
            position = ServerConfigDecoration.ServerConfigDecorationPosition.TC,
            timeout = "5",
            timeoutError = false,
            message = "",
            messageError = false,
            ampm = false,
            showDate = true,
            timeFormat = "HH:mm",
            dateFormat = "yyyy-MM-dd"
        )
    }
}
