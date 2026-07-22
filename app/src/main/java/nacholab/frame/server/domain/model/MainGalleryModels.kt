package nacholab.frame.server.domain.model

import android.net.Uri

sealed class GalleryItem{

    sealed class GalleryItemUri(
        open val uri: Uri,
        open val isRemote: Boolean
    ): GalleryItem()

    data class GalleryItemImage(
        override val uri: Uri,
        override val isRemote: Boolean
    ): GalleryItemUri(uri, isRemote)

    data class GalleryItemVideo(
        override val uri: Uri,
        override val isRemote: Boolean
    ): GalleryItemUri(uri, isRemote)

}

