package nacholab.frame.server.ui

import android.annotation.SuppressLint
import kotlin.math.floor

object TimeFormatter {

    @SuppressLint("DefaultLocale")
    fun millisToHoursMinutesSeconds(millis: Long, showHours: Boolean): String {
        val hours = (millis / (1000 * 60 * 60f)).toInt()
        val minutes = ((millis % (1000 * 60 * 60)) / (1000 * 60)).toInt()
        val seconds = (millis / 1000f - hours * 3600 - minutes * 60).toInt()

        return if (showHours){
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }else{
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    @SuppressLint("DefaultLocale")
    fun minutesToTime(minutes: Int, ampm: Boolean): String {
        val rawHours = floor(minutes / 60.0)
        val remainingMinutes = minutes - rawHours * 60
        val hours = if (ampm && rawHours > 12) rawHours - 12 else rawHours
        val ampm = if (ampm) if (rawHours > 12) "PM" else "AM" else ""
        return String.format("%d:%02d %s", hours.toInt(), remainingMinutes.toInt(), ampm)
    }


}