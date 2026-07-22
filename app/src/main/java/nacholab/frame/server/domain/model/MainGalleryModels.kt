package nacholab.frame.server.domain.model

import android.net.Uri
import java.util.Date

sealed class GalleryItem{

    sealed class GalleryItemUri(
        open val uri: Uri,
        open val isRemote: Boolean,
        open val metadata: GalleryItemMetadata?
    ): GalleryItem()

    data class GalleryItemImage(
        override val uri: Uri,
        override val isRemote: Boolean,
        override val metadata: GalleryItemMetadata? = null
    ): GalleryItemUri(uri, isRemote, metadata)

    data class GalleryItemVideo(
        override val uri: Uri,
        override val isRemote: Boolean,
        override val metadata: GalleryItemMetadata? = null
    ): GalleryItemUri(uri, isRemote, metadata)

}

data class GalleryItemMetadata(
    val date: Date?,
    val location: LatLng?,
    val description: String?,
    val camera: String?
){
    data class LatLng(val lat: Double, val lng: Double)
}

