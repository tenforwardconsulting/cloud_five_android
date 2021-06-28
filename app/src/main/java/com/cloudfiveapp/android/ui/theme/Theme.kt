package com.cloudfiveapp.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val cloudy = true

@Composable
fun CloudFiveTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (cloudy) {
            if (isSystemInDarkTheme()) CloudyDarkThemeColors else CloudyLightThemeColors
        } else {
            if (isSystemInDarkTheme()) DarkThemeColors else LightThemeColors
        },
        shapes = CloudFiveShapes,
        content = content,
    )
}

private val LightThemeColors = lightColors(
    primary = Blue,
    onPrimary = Color.White,
    primaryVariant = DarkBlue,
    secondary = DarkestBlue,
    onSecondary = Color.Black,
    background = Color.White,
    surface = SkyBlue,
    error = DarkRed,
)

private val DarkThemeColors = darkColors(
    primary = DarkestBlue,
    onPrimary = Color.White,
    secondary = SkyBlue,
    onSecondary = Color.White,
    background = DarkestBlue,
    surface = Gray,
    onSurface = Color.White,
    error = DarkRed,
)

private val CloudyLightThemeColors = lightColors(
    primary = DarkestBlue,
    onPrimary = Color.White,
    primaryVariant = DarkBlue,
    secondary = Blue,
    background = SkyBlue,
    surface = Color.White,
    onSurface = Color.Black,
    error = DarkRed,
)

private val CloudyDarkThemeColors = darkColors(
    primary = LightningYellow,
    onPrimary = Color.Black,
    secondary = SkyBlue,
    onSecondary = Color.White,
    background = DarkestBlue,
    surface = Gray,
    onSurface = Color.White,
    error = DarkRed,
)
