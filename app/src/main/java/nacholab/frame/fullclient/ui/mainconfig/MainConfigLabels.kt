package nacholab.frame.fullclient.ui.mainconfig

import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.domain.model.ServerConfigDecoration
import nacholab.frame.domain.model.ServerConfigMainUI

fun ServerConfig.ServerConfigScaling.displayName(): String = when (this) {
    ServerConfig.ServerConfigScaling.CROP -> "Crop"
    ServerConfig.ServerConfigScaling.FIT -> "Fit"
}

fun ServerConfig.ServerConfigSorting.displayName(): String = when (this) {
    ServerConfig.ServerConfigSorting.RANDOM -> "Random"
    ServerConfig.ServerConfigSorting.DATE -> "Date"
    ServerConfig.ServerConfigSorting.NAME -> "Name"
    ServerConfig.ServerConfigSorting.IGNORE -> "Ignore"
}

fun ServerConfigMainUI.ServerConfigMainUIHideType.displayName(): String = when (this) {
    ServerConfigMainUI.ServerConfigMainUIHideType.TIMEOUT -> "Timeout"
    ServerConfigMainUI.ServerConfigMainUIHideType.PAGE_CHANGE -> "Page change"
    ServerConfigMainUI.ServerConfigMainUIHideType.MANUAL -> "Manual"
    ServerConfigMainUI.ServerConfigMainUIHideType.DISABLED -> "Disabled"
}

fun ServerConfigDecoration.ServerConfigDecorationPosition.displayName(): String = when (this) {
    ServerConfigDecoration.ServerConfigDecorationPosition.TS -> "Top start"
    ServerConfigDecoration.ServerConfigDecorationPosition.TC -> "Top center"
    ServerConfigDecoration.ServerConfigDecorationPosition.TE -> "Top end"
    ServerConfigDecoration.ServerConfigDecorationPosition.MS -> "Middle start"
    ServerConfigDecoration.ServerConfigDecorationPosition.MC -> "Middle center"
    ServerConfigDecoration.ServerConfigDecorationPosition.ME -> "Middle end"
    ServerConfigDecoration.ServerConfigDecorationPosition.BS -> "Bottom start"
    ServerConfigDecoration.ServerConfigDecorationPosition.BC -> "Bottom center"
    ServerConfigDecoration.ServerConfigDecorationPosition.BE -> "Bottom end"
}

fun DecorationDraftState.DecorationKind.displayName(): String = when (this) {
    DecorationDraftState.DecorationKind.CURRENT_DATE_TIME -> "Date & time"
    DecorationDraftState.DecorationKind.MESSAGE -> "Message"
    DecorationDraftState.DecorationKind.MEDIA_INFO -> "Media info"
}

fun ServerConfigDecoration.summary(): String {
    val kind = when (this) {
        is ServerConfigDecoration.CurrentDateTime -> "Date & time"
        is ServerConfigDecoration.Message -> "Message: \"$message\""
        is ServerConfigDecoration.MediaInfo -> "Media info"
    }
    return "$kind — ${position.displayName()} (${timeout}s)"
}
