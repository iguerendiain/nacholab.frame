package nacholab.frame.usecases

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class RequestDirToUserUseCase(private val activity: ComponentActivity){

    suspend fun execute() = suspendCancellableCoroutine { c ->
        activity.registerForActivityResult(
            ActivityResultContracts.OpenDocumentTree()
        ) { uri ->

            if (uri != null) {

                activity.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                c.resume(buildDocumentFileFromUri(uri))
            }
        }.launch(null)
    }

    fun buildDocumentFileFromUri(uri: Uri) = DocumentFile
        .fromTreeUri(
            activity,
            uri
        )
}