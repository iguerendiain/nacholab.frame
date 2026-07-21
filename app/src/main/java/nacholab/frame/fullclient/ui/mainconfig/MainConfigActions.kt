package nacholab.frame.fullclient.ui.mainconfig

import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.domain.model.ServerConfigMainUI

sealed class MainConfigActions {

    data class SetFrameName(val frameName: String) : MainConfigActions()

    data class SetAutoSync(val autoSync: Boolean) : MainConfigActions()

    data class SetMediaItemTime(val mediaItemTime: String) : MainConfigActions()

    data class SetReshuffleAfterPlaylistFinish(val reshuffle: Boolean) : MainConfigActions()

    data class SetSleepTimerFrom(val sleepTimerFrom: String) : MainConfigActions()

    data class SetSleepTimerTo(val sleepTimerTo: String) : MainConfigActions()

    data class SetImageScaling(val scaling: ServerConfig.ServerConfigScaling) : MainConfigActions()

    data class SetVideoScaling(val scaling: ServerConfig.ServerConfigScaling) : MainConfigActions()

    data class SetSortType(val sortType: ServerConfig.ServerConfigSorting) : MainConfigActions()

    data class SetDirSortType(val dirSortType: ServerConfig.ServerConfigSorting) : MainConfigActions()

    data class SetMainUIHideType(val hideType: String) : MainConfigActions()

    data class SetMainUIHideTimeout(
        val hideTimeout: ServerConfigMainUI.ServerConfigMainUIHideType
    ) : MainConfigActions()

    data class SetDecorationDraftKind(val kind: DecorationDraftState.DecorationKind) : MainConfigActions()

    data class SetDecorationDraftPosition(
        val position: ServerConfigDecoration.ServerConfigDecorationPosition
    ) : MainConfigActions()

    data class SetDecorationDraftTimeout(val timeout: String) : MainConfigActions()

    data class SetDecorationDraftMessage(val message: String) : MainConfigActions()

    data class SetDecorationDraftAmPm(val ampm: Boolean) : MainConfigActions()

    data class SetDecorationDraftShowDate(val showDate: Boolean) : MainConfigActions()

    data class SetDecorationDraftTimeFormat(val timeFormat: String) : MainConfigActions()

    data class SetDecorationDraftDateFormat(val dateFormat: String) : MainConfigActions()

    object AddDecoration : MainConfigActions()

    data class RemoveDecoration(val index: Int) : MainConfigActions()

    object SaveSettings : MainConfigActions()

    object ChangeServer : MainConfigActions()

}
