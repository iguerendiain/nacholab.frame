package nacholab.frame.data

import androidx.documentfile.provider.DocumentFile

class MediaItemRepository {

    private val mediaItems: ArrayList<GalleryItem> = arrayListOf()
    fun buildMediaGalleryItems(documentDir: DocumentFile){
        return documentDir
            .listFiles()
            .forEach {
                when{
                    it.isDirectory -> buildMediaGalleryItems(it)
                    it.isFile && it.type?.startsWith("image/") == true -> {
                        mediaItems
                            .add(
                                GalleryItem.GalleryItemImage(
                                    uri = it.uri,
                                    isRemote = false
                                )
                            )
                    }
                    it.isFile && it.type?.startsWith("video/") == true -> {
                        mediaItems
                            .add(
                                GalleryItem.GalleryItemVideo(
                                uri = it.uri,
                                isRemote = false
                            ))
                    }
                }
            }
    }

    fun getCurrentMediaItems() = mediaItems

}