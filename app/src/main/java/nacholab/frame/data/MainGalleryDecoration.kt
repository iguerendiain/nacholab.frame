package nacholab.frame.data

import java.time.format.FormatStyle

sealed class MainGalleryDecoration(
    open val position: Position,
    open val timeout: Int?
){
    enum class Position{
        TOP_START,
        TOP_CENTER,
        TOP_END,
        MIDDLE_START,
        MIDDLE_CENTER,
        MIDDLE_END,
        BOTTOM_START,
        BOTTOM_CENTER,
        BOTTOM_END,
    }

    data class CurrentTime(
        override val position: Position,
        override val timeout: Int?,
        val ampm: Boolean,
        val showDate: Boolean,
        val timeFormat: String,
        val dateFormat: String
    ): MainGalleryDecoration(
        position = position,
        timeout = timeout
    )

    data class Message(
        override val position: Position,
        override val timeout: Int?,
        val message: String
    ): MainGalleryDecoration(
        position = position,
        timeout = timeout
    )

    data class MediaInfo(
        override val position: Position,
        override val timeout: Int?,
        val data: List<MediaInfoData>,
        val timeTakenFormat: String,
        val dateTakenFormat: String
    ): MainGalleryDecoration(
        position = position,
        timeout = timeout
    ){
        enum class MediaInfoData{
            FILENAME,
            TYPE,
            POSITION_IN_GALLERY,
            DURATION,
            RESOLUTION,
            TIME_TAKEN,
            DATE_TAKEN,
            ITEM_PROGRESS
        }
    }

}