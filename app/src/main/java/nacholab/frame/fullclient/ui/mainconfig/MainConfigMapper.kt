package nacholab.frame.fullclient.ui.mainconfig

import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerConfigMainUI

object MainConfigMapper{

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