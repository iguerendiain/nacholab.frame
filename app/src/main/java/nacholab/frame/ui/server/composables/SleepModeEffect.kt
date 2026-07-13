package nacholab.frame.ui.server.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import java.time.LocalTime

@Composable
fun SleepModeEffect(sleepFrom: Int?, sleepTo: Int?, cb: (Boolean) -> Unit){
    LaunchedEffect(sleepFrom, sleepTo) {
        while (sleepFrom!=null && sleepTo!=null) {
            val now = LocalTime.now()
            val currentMinute = now.hour * 60 + now.minute
            cb(currentMinute in sleepFrom..sleepTo)
            delay(60_000L)
        }
    }
}