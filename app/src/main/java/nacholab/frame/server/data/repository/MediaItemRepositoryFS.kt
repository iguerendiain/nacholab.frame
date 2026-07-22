package nacholab.frame.server.data.repository

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import nacholab.frame.server.domain.model.GalleryItem
import nacholab.frame.server.domain.model.GalleryItemMetadata
import nacholab.frame.server.domain.repository.MediaItemRepository
import nacholab.frame.utils.MetadataTools

class MediaItemRepositoryFS(
    private val context: Context
): MediaItemRepository {

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
                                    isRemote = false,
                                    metadata = MetadataTools.extractImageExifData(context, it.uri)?.toDomain()
                                )
                            )
                    }
                    it.isFile && it.type?.startsWith("video/") == true -> {
                        mediaItems
                            .add(
                                GalleryItem.GalleryItemVideo(
                                uri = it.uri,
                                isRemote = false,
                                metadata = MetadataTools.extractVideoMetadata(context, it.uri)?.toDomain()
                            ))
                    }
                }
            }
    }

    override fun getCurrentMediaItems() = mediaItems

}

private fun MetadataTools.FileMetaData.toDomain() = GalleryItemMetadata(
    date = date,
    location = location?.let { GalleryItemMetadata.LatLng(it.lat, it.lng) },
    description = description,
    camera = camera
)