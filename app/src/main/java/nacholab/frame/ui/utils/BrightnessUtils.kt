package nacholab.frame.ui.utils

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

object BrightnessUtils {
    fun setScreenBrightness(context: Context, brightness: Float) {
        val activity = context as? Activity ?: return
        val layoutParams: WindowManager.LayoutParams = activity.window.attributes
        layoutParams.screenBrightness = brightness.coerceIn(-1f, 1f)
        activity.window.attributes = layoutParams
    }

    fun getScreenBrightness(context: Context): Float {
        val activity = context as? Activity ?: return 1f
        val layoutParams: WindowManager.LayoutParams = activity.window.attributes
        return layoutParams.screenBrightness
    }
}

@Composable
fun BrightnessLaunchEffect(brightness: Float){
    val context = LocalContext.current
    LaunchedEffect(brightness) {
        BrightnessUtils.setScreenBrightness(context, brightness)
    }
}