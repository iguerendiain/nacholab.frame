package nacholab.frame.fullclient.ui.mainconfig

data class MainConfigState(
    val host: String,
    val port: Int?,
    val frameName: String,
    val autoSync: Boolean
) {
    companion object {
        val DEFAULT = MainConfigState(
            host = "",
            port = null,
            frameName = "",
            autoSync = true
        )
    }
}
