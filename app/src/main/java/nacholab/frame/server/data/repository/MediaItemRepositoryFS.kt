package nacholab.frame.server.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.server.domain.model.GalleryItem
import nacholab.frame.server.domain.model.GalleryItemMetadata
import nacholab.frame.server.domain.repository.MediaItemRepository
import nacholab.frame.utils.MetadataTools
import java.util.Date
import kotlin.random.Random

class MediaItemRepositoryFS(
    private val context: Context
): MediaItemRepository {

    private val mediaItems: ArrayList<GalleryItem> = arrayListOf()

    override fun buildMediaGalleryItemsNoDirSorted(
        documentDir: DocumentFile,
        sortFileType: ServerConfig.ServerConfigSorting
    ) {
        documentDir
            .listFiles()
            .forEach {
                when{
                    it.isDirectory -> buildMediaGalleryItemsNoDirSorted(it, sortFileType)
                    it.isFile -> buildMediaGallery(it)
                }
            }

        val sortedMediaItems = when (sortFileType){
            ServerConfig.ServerConfigSorting.RANDOM -> mediaItems.sortedBy { Random.nextInt() }
            ServerConfig.ServerConfigSorting.DATE -> mediaItems.sortedBy {
                when (it) {
                    is GalleryItem.GalleryItemVideo -> it.metadata?.date
                    is GalleryItem.GalleryItemImage -> it.metadata?.date
                }
            }
            ServerConfig.ServerConfigSorting.NAME -> mediaItems.sortedBy {
                if (it is GalleryItem.GalleryItemUri){
                    it.uri.toFile().absolutePath.split("/").last()
                }else null
            }
            ServerConfig.ServerConfigSorting.IGNORE -> mediaItems
        }

        mediaItems.clear()
        mediaItems.addAll(sortedMediaItems)
    }

    override fun buildMediaGalleryItemsDirSorted(
        documentDir: DocumentFile,
        sortDirType: ServerConfig.ServerConfigSorting,
        sortFileType: ServerConfig.ServerConfigSorting
    ){
        listDirectories(documentDir).sortedBy(sortDirType).forEach { dir ->
            buildMediaGalleryItemsDirSorted(dir, sortDirType, sortFileType)
        }

        buildMediaGalleryItemsFileSorted(documentDir, sortFileType)
    }

    override fun getCurrentMediaItems() = mediaItems

    private fun List<DocumentFile>.sortedBy(sort: ServerConfig.ServerConfigSorting) = when (sort) {
        ServerConfig.ServerConfigSorting.RANDOM -> sortedBy { Random.nextInt() }
        ServerConfig.ServerConfigSorting.DATE -> sortedBy { it.uri.getFileDate() }
        ServerConfig.ServerConfigSorting.NAME -> sortedBy { it.name?.split("/")?.last() }
        ServerConfig.ServerConfigSorting.IGNORE -> this
    }

    private fun Uri.getFileDate(): Date {
        val exifDate = MetadataTools
            .extractImageExifData(context, this)
            ?.toDomain()
            ?.date

        val metaDataDate = MetadataTools
            .extractVideoMetadata(context, this)
            ?.toDomain()
            ?.date

        val fileDate = toFile()
            .lastModified()
            .let { Date(it) }

        return exifDate ?: metaDataDate ?: fileDate
    }

    private fun buildMediaGalleryItemsFileSorted(
        documentDir: DocumentFile,
        sortType: ServerConfig.ServerConfigSorting,
    ){
        listMediaFiles(documentDir).sortedBy(sortType).forEach {
            buildMediaGallery(it)
        }
    }

    private fun buildMediaGallery(documentDir: DocumentFile){
        when{
            documentDir.type?.startsWith("image/") == true -> {
                mediaItems
                    .add(
                        GalleryItem.GalleryItemImage(
                            uri = documentDir.uri,
                            isRemote = false,
                            metadata = MetadataTools
                                .extractImageExifData(context, documentDir.uri)
                                ?.toDomain()
                        )
                    )
            }
            documentDir.type?.startsWith("video/") == true -> {
                mediaItems
                    .add(
                        GalleryItem.GalleryItemVideo(
                            uri = documentDir.uri,
                            isRemote = false,
                            metadata = MetadataTools
                                .extractVideoMetadata(context, documentDir.uri)
                                ?.toDomain()
                        ))
            }
        }
    }

    private fun listDirectories(documentDir: DocumentFile) = documentDir
        .listFiles()
        .filter { it.isDirectory }

    private fun listMediaFiles(documentDir: DocumentFile) = documentDir
        .listFiles()
        .filter {
            it.isFile && (
                    it.type?.startsWith("image/") == true ||
                    it.type?.startsWith("video/") == true
            )
        }
}

private fun MetadataTools.FileMetaData.toDomain() = GalleryItemMetadata(
    date = date,
    location = location?.let { GalleryItemMetadata.LatLng(it.lat, it.lng) },
    description = description,
    camera = camera
)