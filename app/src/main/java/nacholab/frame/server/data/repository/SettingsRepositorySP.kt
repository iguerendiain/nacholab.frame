package nacholab.frame.server.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import nacholab.frame.server.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositorySP @Inject constructor(
    private val sharedPreferences: SharedPreferences
): SettingsRepository {

    companion object {
        const val KEY_MAIN_MEDIA_DIR = "mainMediaDir"
    }

    override fun saveFolderUri(uri: String?) {
        sharedPreferences.edit {
            putString(KEY_MAIN_MEDIA_DIR, uri)
        }
    }

    override fun getSavedFolderUri() = sharedPreferences.getString(KEY_MAIN_MEDIA_DIR, null)

}