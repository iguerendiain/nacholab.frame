package nacholab.frame.server.domain.repository

interface SettingsRepository{

    fun saveFolderUri(uri: String?)

    fun getSavedFolderUri(): String?

}