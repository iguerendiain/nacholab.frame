package nacholab.frame.data

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val KEY_MAIN_MEDIA_DIR = "mainMediaDir"
    }

    fun saveFolderUri(uri: String?) {
        sharedPreferences.edit {
            putString(KEY_MAIN_MEDIA_DIR, uri)
        }
    }

    fun getSavedFolderUri() = sharedPreferences.getString(KEY_MAIN_MEDIA_DIR, null)

}