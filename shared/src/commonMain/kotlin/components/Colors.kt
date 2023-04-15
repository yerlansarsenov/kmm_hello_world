package components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val progressBackgroundColor: Color
    @Composable
    get() = componentColor(
        light = Color(0xFF_454548),
        dark = Color(0xFF_A1A1AF)
    )

val labelColor: Color
    @Composable
    get() = componentColor(
        light = Color(0xFF_99A2AD),
        dark = Color(0xFF_98989F)
    )

val backgroundColor: Color
    @Composable
    get() = componentColor(
        light = Color(0xFF_FFFFFF),
        dark = Color(0xFF_161618),
    )

@Composable
private inline fun componentColor(
    light: Color,
    dark: Color
): Color = if (isSystemInDarkTheme()) {
    dark
} else {
    light
}
