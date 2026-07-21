package nacholab.frame.server.ui

import nacholab.frame.data.GalleryItem
import nacholab.frame.data.LoadingState
import nacholab.frame.data.MainGalleryDecoration
import nacholab.frame.data.MainGalleryDecoration.Position

data class ServerAppState(
    val loadingState: LoadingState,
    val currentGallery: List<GalleryItem>,
    val isPlaying: Boolean,
    val videoPosition: Float,
    val brightness: Float,
    val volume: Float,
    val isMuted: Boolean,
    val sleepFrom: Int?,
    val sleepTo: Int?,
    val sleepMode: Boolean,
    val ampm: Boolean,
    val minuteClock: Int,
    val decorations: List<MainGalleryDecoration>,
){
    companion object{
        val DEFAULT = ServerAppState(
            loadingState = LoadingState.Loading,
            currentGallery = listOf(),
            isPlaying = true,
            videoPosition = 0f,
            brightness = 1f,
            volume = 1f,
            isMuted = true,
            sleepMode = false,
            sleepFrom = 960,
            sleepTo = 990,
            ampm = true,
            minuteClock = -1,
            decorations = listOf(
                MainGalleryDecoration.MediaInfo(
                    position = Position.TOP_START,
                    timeout = null,
                    data = listOf(),
                    timeTakenFormat = DEMO_TIME_FORMAT,
                    dateTakenFormat = DEMO_DATE_FORMAT,
                ),
                MainGalleryDecoration.Message(
                    position = Position.TOP_CENTER,
                    timeout = null,
                    message = "Este es un mensaje que está arriba en el medio"
                ),
                MainGalleryDecoration.CurrentTime(
                    position = Position.TOP_END,
                    timeout = null,
                    ampm = false,
                    showDate = false,
                    timeFormat = DEMO_TIME_FORMAT,
                    dateFormat = DEMO_DATE_FORMAT
                ),
                MainGalleryDecoration.MediaInfo(
                    position = Position.MIDDLE_START,
                    timeout = null,
                    data = listOf(),
                    timeTakenFormat = DEMO_TIME_FORMAT,
                    dateTakenFormat = DEMO_DATE_FORMAT,
                ),
                MainGalleryDecoration.Message(
                    position = Position.MIDDLE_CENTER,
                    timeout = null,
                    message = "HOLA, Estoy en el centro de todo"
                ),
                MainGalleryDecoration.Message(
                    position = Position.MIDDLE_END,
                    timeout = null,
                    message = "HoooOOOooolaaaa"
                ),
                MainGalleryDecoration.CurrentTime(
                    position = Position.BOTTOM_START,
                    timeout = null,
                    ampm = true,
                    showDate = true,
                    timeFormat = DEMO_TIME_FORMAT,
                    dateFormat = DEMO_DATE_FORMAT
                ),
                MainGalleryDecoration.Message(
                    position = Position.BOTTOM_CENTER,
                    timeout = null,
                    message = "¡MENSAJE!"
                ),
                MainGalleryDecoration.MediaInfo(
                    position = Position.BOTTOM_END,
                    timeout = null,
                    data = listOf(),
                    timeTakenFormat = DEMO_TIME_FORMAT,
                    dateTakenFormat = DEMO_DATE_FORMAT
                )
            )
        )
    }
}

const val DEMO_TIME_FORMAT = "h:mm"
const val DEMO_DATE_FORMAT = "dd/MM/yyyy"