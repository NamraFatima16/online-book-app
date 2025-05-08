package ie.setu.bookapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = Primary.copy(alpha = 0.2f),
    onPrimaryContainer = Primary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = Secondary.copy(alpha = 0.2f),
    onSecondaryContainer = Secondary,
    tertiary = Primary.copy(alpha = 0.6f),
    onTertiary = OnPrimary,
    tertiaryContainer = Primary.copy(alpha = 0.1f),
    onTertiaryContainer = Primary.copy(alpha = 0.8f),
    error = Error,
    onError = OnError,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = Surface.copy(alpha = 0.3f),
    onSurfaceVariant = OnSurface.copy(alpha = 0.7f),
    outline = OnSurface.copy(alpha = 0.2f)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark_Dark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryDark_Dark.copy(alpha = 0.2f),
    onPrimaryContainer = PrimaryDark_Dark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryDark.copy(alpha = 0.2f),
    onSecondaryContainer = SecondaryDark,
    tertiary = PrimaryDark_Dark.copy(alpha = 0.6f),
    onTertiary = OnPrimaryDark,
    tertiaryContainer = PrimaryDark_Dark.copy(alpha = 0.1f),
    onTertiaryContainer = PrimaryDark_Dark.copy(alpha = 0.8f),
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorDark.copy(alpha = 0.1f),
    onErrorContainer = ErrorDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceDark.copy(alpha = 0.3f),
    onSurfaceVariant = OnSurfaceDark.copy(alpha = 0.7f),
    outline = OnSurfaceDark.copy(alpha = 0.2f)
)

@Composable
fun BookAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}