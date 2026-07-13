package nacholab.frame.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.exifinterface.media.ExifInterface
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MetadataToolsTools {

    data class FileMetaData(
        val date: Date?,
        val location: LatLng?,
        val description: String?,
        val camera: String?
    ){
        data class LatLng(val lat: Double, val lng: Double)
    }

    @Composable
    fun extractImageExifData(uri: Uri) = extractImageExifData(LocalContext.current, uri)

    @Composable
    fun extractVideoMetadata(uri: Uri) = extractVideoMetadata(LocalContext.current, uri)

    fun extractImageExifData(context: Context, uri: Uri): FileMetaData? {
        return try{
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                val exif = ExifInterface(stream)

                FileMetaData(
                    date = extractExifDateTime(exif),
                    location = extractExifLocation(exif),
                    description = extractExifDescription(exif),
                    camera = extractExifCamera(exif)
                )
            }
        }catch (e: Exception){
            null
        }
    }

    fun extractVideoMetadata(context: Context, uri: Uri): FileMetaData? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)

            FileMetaData(
                date = extractVideoMetadataDate(retriever),
                location = extractVideoMetadataLocation(retriever),
                description = extractVideoMetadataDescription(retriever),
                camera = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
            null
        }
    }

    fun extractVideoMetadataDescription(retriever: MediaMetadataRetriever): String? {
        return try{
            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val writer = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)

            if (title!=null && writer!=null) "$title ($writer)"
            else title?:writer
        }catch (e: Exception){
            null
        }
    }

    fun extractVideoMetadataLocation(retriever: MediaMetadataRetriever): FileMetaData.LatLng? {
        return try{
            retriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION)
                ?.split("/")
                ?.first()
                ?.split(",")
                ?.map { it.toDouble() }
                ?.let {
                    FileMetaData.LatLng(it[0], it[1])
                }
        }catch (e: Exception){
            null
        }
    }

    fun extractVideoMetadataDate(retriever: MediaMetadataRetriever): Date? {
        val date = try{
            retriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)
                ?.split("T")
                ?.first()
                ?.let {
                    val format = SimpleDateFormat("yyyyMMddTHHmmss", Locale.US)
                    format.parse(it)
                }
        }catch (e: Exception){
            null
        }

        val year = try{
            retriever
                .extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)
                ?.let {
                    val format = SimpleDateFormat("yyyy", Locale.US)
                    format.parse(it)
                }
        }catch (e: Exception){
            null
        }

        return date?:year
    }

    @SuppressLint("RestrictedApi")
    fun extractExifCamera(exif: ExifInterface): String? {
        return try{
            exif.getAttribute(ExifInterface.TAG_CAMERA_OWNER_NAME)
        }catch (e: Exception){
            null
        }
    }

    fun extractExifDescription(exif: ExifInterface): String? {
        return try{
            exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION)
        }catch (e: Exception){
            null
        }
    }

    fun extractExifLocation(exif: ExifInterface): FileMetaData.LatLng? {
        return try {
            val lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            val latRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
            val lng = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            val lngRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)

            val latitude = (lat?:latRef)?.toDouble()
            val longitude = (lng?:lngRef)?.toDouble()

            if (latitude != null && longitude != null){
                FileMetaData.LatLng(latitude, longitude)
            }else{
                null
            }
        }catch (e: Exception){
            null
        }
    }

    @SuppressLint("RestrictedApi")
    fun extractExifDateTime(exif: ExifInterface): Date? {
        return try {
            val tagDateTimeOriginal = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
            val dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME)
            val digitized = exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)

            val exifDate = (tagDateTimeOriginal ?: dateTime ?: digitized)?.let {
                val format = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US)
                format.parse(it)
            }

            val timestampDate = exif.dateTimeOriginal.takeIf { it != -1L }?.let {
                Date(it)
            }

            exifDate ?: timestampDate
        }catch (e: Exception){
            null
        }
    }

}