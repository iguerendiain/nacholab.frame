package nacholab.frame.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NavyPrimaryDark,
    onPrimary = BrandNavy,
    primaryContainer = NavyPrimaryContainerDark,
    onPrimaryContainer = NavyOnPrimaryContainerDark,
    secondary = NavySecondaryDark,
    onSecondary = NavyOnSecondaryDark,
    secondaryContainer = NavySecondaryContainerDark,
    onSecondaryContainer = NavyOnSecondaryContainerDark,
    tertiary = NavyTertiaryDark,
    onTertiary = NavyOnTertiaryDark,
    tertiaryContainer = NavyTertiaryContainerDark,
    onTertiaryContainer = NavyOnTertiaryContainerDark,
    background = NavyBackgroundDark,
    onBackground = BrandWhite,
    surface = NavyBackgroundDark,
    onSurface = BrandWhite,
    surfaceVariant = NavySurfaceVariantDark,
    onSurfaceVariant = NavyOnSurfaceVariantDark,
    outline = NavyOutlineDark,
    outlineVariant = NavyOutlineVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = BrandNavy,
    onPrimary = BrandWhite,
    primaryContainer = NavyPrimaryContainerLight,
    onPrimaryContainer = NavyOnPrimaryContainerLight,
    secondary = NavySecondaryLight,
    onSecondary = BrandWhite,
    secondaryContainer = NavySecondaryContainerLight,
    onSecondaryContainer = NavyOnSecondaryContainerLight,
    tertiary = NavyTertiaryLight,
    onTertiary = BrandWhite,
    tertiaryContainer = NavyTertiaryContainerLight,
    onTertiaryContainer = NavyOnTertiaryContainerLight,
    background = BrandWhite,
    onBackground = NavyOnBackgroundLight,
    surface = BrandWhite,
    onSurface = NavyOnBackgroundLight,
    surfaceVariant = NavySurfaceVariantLight,
    onSurfaceVariant = NavyOnSurfaceVariantLight,
    outline = NavyOutlineLight,
    outlineVariant = NavyOutlineVariantLight
)

/**
 * Single source of truth for the app's Material 3 theme, shared by every activity
 * (server, full client, and future screens).
 */
@Composable
fun NacholabFrameTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Brand identity (navy + white) takes precedence over the device wallpaper by default.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = NacholabFrameShapes,
        content = content
    )
}
