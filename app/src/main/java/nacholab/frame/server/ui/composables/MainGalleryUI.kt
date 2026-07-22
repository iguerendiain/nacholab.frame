package nacholab.frame.server.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nacholab.frame.R
import nacholab.frame.server.ui.TimeFormatter

@Composable
fun MainGalleryUI(
    modifier: Modifier = Modifier,
    title: String,
    isPlaying: Boolean,
    isVideo: Boolean,
    isMuted: Boolean,
    currentVolume: Float,
    currentBrightness: Float,
    currentVideoPosition: Long,
    currentVideoDuration: Long,
    togglePlayback: () -> Unit,
    scrubVideo: (position: Float) -> Unit,
    toggleMute: () -> Unit,
    changeVolume: (volume: Float) -> Unit,
    changeBrightness: (brightness: Float) -> Unit,
    sleep: () -> Unit,
    currentTimeMinutes: Int,
    ampm: Boolean,
    sleepFrom: Int?,
    sleepTo: Int?,
    previousPage: () -> Unit,
    nextPage: () -> Unit
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = title,
                color = White,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f).padding(8.dp)
            )

            Text(
                text = TimeFormatter.minutesToTime(currentTimeMinutes, ampm),
                color = White,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )

            if (sleepFrom!=null && sleepTo!=null) Text(
                text = "(%s - %s)".format(
                    TimeFormatter.minutesToTime(sleepFrom, ampm),
                    TimeFormatter.minutesToTime(sleepTo, ampm)
                ),
                color = White,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )

            Image(
                painter = painterResource(R.drawable.ic_moon),
                contentDescription = "",
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier
                    .clickable { sleep() }
                    .padding(8.dp)
                    .size(32.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ){
            Image(
                painter = painterResource(R.drawable.ic_speaker),
                contentDescription = "",
                colorFilter = ColorFilter.tint(
                    if (isMuted) Gray else White
                ),
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        toggleMute()
                    }
            )

            Text(
                text = "-",
                color = White,
                fontSize = 72.sp,
                modifier = Modifier.clickable {
                    changeVolume(currentVolume - .25f)
                }
            )

            Scrubber(
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp),
                position = currentVolume,
                onValueChange = changeVolume
            )

            Text(
                text = "+",
                color = White,
                fontSize = 72.sp,
                modifier = Modifier.clickable {
                    changeVolume(currentVolume + .25f)
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.ic_bright_increase),
                contentDescription = "",
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "-",
                color = White,
                fontSize = 72.sp,
                modifier = Modifier.clickable {
                    changeBrightness(currentBrightness - .25f)
                }
            )

            Scrubber(
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp),
                position = currentBrightness,
                onValueChange = changeBrightness
            )

            Text(
                text = "+",
                color = White,
                fontSize = 72.sp,
                modifier = Modifier.clickable {
                    changeBrightness(currentBrightness + .25f)
                }
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Image(
                painter = painterResource(R.drawable.ic_chevron_left),
                contentDescription = "",
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier
                    .size(104.dp)
                    .clickable {
                        previousPage()
                    }
            )
            Image(
                painter = painterResource(
                    if (isPlaying) R.drawable.ic_pause_circle
                        else R.drawable.ic_play_circle
                ),
                contentDescription = "",
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier
                    .size(104.dp)
                    .clickable {
                        togglePlayback()
                    }
            )
            Image(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = "",
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier
                    .size(104.dp)
                    .clickable {
                        nextPage()
                    }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ){
            if (isVideo){
                val showHours = currentVideoDuration > 3600000
                Text(
                    text = TimeFormatter.millisToHoursMinutesSeconds(currentVideoPosition, showHours),
                    color = White,
                    fontSize = 18.sp
                )
                Scrubber(
                    modifier = Modifier
                        .weight(1f)
                        .height(24.dp),
                    position = currentVideoPosition / currentVideoDuration.toFloat(),
                    onValueChange = scrubVideo
                )
                Text(
                    text = TimeFormatter.millisToHoursMinutesSeconds(currentVideoDuration, showHours),
                    color = White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(
    showSystemUi = false,
    device = "spec:width=480dp,height=800dp,dpi=420,orientation=landscape"
)
@Composable
fun MainGalleryUIPreview(){
    MaterialTheme {
        MainGalleryUI(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .6f)),
            title = "zaraza.jpg (Foto) 4/4387",
            isPlaying = true,
            isVideo = true,
            isMuted = true,
            currentVolume = .6f,
            currentBrightness = 1f,
            currentVideoPosition = 1579000L,
            currentVideoDuration = 4877000L,
            sleepFrom = 1041,
            sleepTo = 1072,
            currentTimeMinutes = 934,
            ampm = true,
            togglePlayback = { },
            scrubVideo = { },
            toggleMute = { },
            changeVolume = { },
            changeBrightness = { },
            previousPage = { },
            nextPage = { },
            sleep = { },
        )
    }
}