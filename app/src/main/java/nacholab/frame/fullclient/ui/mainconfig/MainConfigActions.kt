package nacholab.frame.fullclient.ui.mainconfig

sealed class MainConfigActions {

    data class SetFrameName(val frameName: String) : MainConfigActions()

    data class SetAutoSync(val autoSync: Boolean) : MainConfigActions()

    object SaveSettings : MainConfigActions()

    object ChangeServer : MainConfigActions()

}
