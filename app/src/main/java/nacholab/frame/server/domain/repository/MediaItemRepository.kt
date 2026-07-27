package nacholab.frame.server.domain.repository

import androidx.documentfile.provider.DocumentFile
import nacholab.frame.domain.model.ServerConfig
import nacholab.frame.server.domain.model.GalleryItem

interface MediaItemRepository {

    fun buildMediaGalleryItemsNoDirSorted(
        documentDir: DocumentFile,
        sortFileType: ServerConfig.ServerConfigSorting
    )

    fun buildMediaGalleryItemsDirSorted(
        documentDir: DocumentFile,
        sortDirType: ServerConfig.ServerConfigSorting,
        sortFileType: ServerConfig.ServerConfigSorting
    )

    fun getCurrentMediaItems(): ArrayList<GalleryItem>
}