package nacholab.frame.server.domain.repository

import androidx.documentfile.provider.DocumentFile
import nacholab.frame.server.domain.model.GalleryItem

interface MediaItemRepository {

    fun buildMediaGalleryItems(documentDir: DocumentFile)

    fun getCurrentMediaItems(): List<GalleryItem>

}