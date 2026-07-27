package nacholab.frame.fullclient.ui.mainconfig

import androidx.compose.ui.layout.ContentScale
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerConfig.ServerConfigScaling
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.domain.model.ServerConfigMainUI
import nacholab.frame.server.ui.models.MainGalleryDecoration

object MainConfigMapper{

    fun buildFrom(scaling: ServerConfigScaling) = when (scaling){
        ServerConfigScaling.CROP -> ContentScale.Crop
        ServerConfigScaling.FIT -> ContentScale.Fit
    }

    fun buildFrom(serverDecoration: ServerConfigDecoration) = when (serverDecoration){
        is ServerConfigDecoration.CurrentDateTime -> MainGalleryDecoration.CurrentTime(
            position = buildFrom(serverDecoration.position),
            timeout = serverDecoration.timeout,
            ampm = serverDecoration.ampm,
            showDate = serverDecoration.showDate,
            timeFormat = serverDecoration.timeFormat,
            dateFormat = serverDecoration.dateFormat
        )
        is ServerConfigDecoration.Message -> MainGalleryDecoration.Message(
            position = buildFrom(serverDecoration.position),
            timeout = serverDecoration.timeout,
            message = serverDecoration.message
        )
        is ServerConfigDecoration.MediaInfo -> MainGalleryDecoration.MediaInfo(
            position = buildFrom(serverDecoration.position),
            timeout = serverDecoration.timeout,
            data = listOf(),
            timeTakenFormat = serverDecoration.timeFormat,
            dateTakenFormat = serverDecoration.dateFormat
        )
    }

    fun buildFrom(serverPosition: ServerConfigDecoration.ServerConfigDecorationPosition) = when (serverPosition){
        ServerConfigDecoration.ServerConfigDecorationPosition.TS -> MainGalleryDecoration.Position.TOP_START
        ServerConfigDecoration.ServerConfigDecorationPosition.TC -> MainGalleryDecoration.Position.TOP_CENTER
        ServerConfigDecoration.ServerConfigDecorationPosition.TE -> MainGalleryDecoration.Position.TOP_END
        ServerConfigDecoration.ServerConfigDecorationPosition.MS -> MainGalleryDecoration.Position.MIDDLE_START
        ServerConfigDecoration.ServerConfigDecorationPosition.MC -> MainGalleryDecoration.Position.MIDDLE_CENTER
        ServerConfigDecoration.ServerConfigDecorationPosition.ME -> MainGalleryDecoration.Position.MIDDLE_END
        ServerConfigDecoration.ServerConfigDecorationPosition.BS -> MainGalleryDecoration.Position.BOTTOM_START
        ServerConfigDecoration.ServerConfigDecorationPosition.BC -> MainGalleryDecoration.Position.BOTTOM_CENTER
        ServerConfigDecoration.ServerConfigDecorationPosition.BE -> MainGalleryDecoration.Position.BOTTOM_END
    }

    fun buildFrom(state: MainConfigState) = ServerConfig(
        decorations = state.decorations,
        mainUI = ServerConfigMainUI(
            hideType = state.mainUIHideType,
            hideTimeout = state.mainUIHideTimeout
        ),
        mediaItemTime = state.mediaItemTime,
        reshuffleAfterPlaylistFinish = state.reshuffleAfterPlaylistFinish,
        sleepTimerFrom = buildFrom(state.sleepTimerFromHour, state.sleepTimerFromMinute),
        sleepTimerTo = buildFrom(state.sleepTimerToHour, state.sleepTimerToMinute),
        imageScaling = state.imageScaling,
        videoScaling = state.videoScaling,
        sortType = state.sortType,
        dirSortType = if (state.directorySortEnabled)
            state.dirSortType
        else
            ServerConfig.ServerConfigSorting.IGNORE
    )

    fun buildFrom(hours: Int, minutes: Int) = hours * 60 + minutes

}