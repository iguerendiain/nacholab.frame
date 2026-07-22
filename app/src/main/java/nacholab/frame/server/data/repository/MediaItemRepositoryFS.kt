package nacholab.frame.server.data.repository

import androidx.documentfile.provider.DocumentFile
import nacholab.frame.server.domain.model.GalleryItem
import nacholab.frame.server.domain.repository.MediaItemRepository

class MediaItemRepositoryFS: MediaItemRepository {

    private val mediaItems: ArrayList<GalleryItem> = arrayListOf()
    override fun buildMediaGalleryItems(documentDir: DocumentFile){
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

    override fun getCurrentMediaItems() = mediaItems

}