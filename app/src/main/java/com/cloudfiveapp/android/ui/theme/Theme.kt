package com.cloudfiveapp.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun CloudFiveTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkThemeColors else LightThemeColors,
        shapes = CloudFiveShapes,
        content = content,
    )
}

private val LightThemeColors = lightColors(
    primary = SkyBlue,
    onPrimary = Color.White,
    primaryVariant = SkyBlue,
    secondary = Blue,
    onSecondary = Color.White,
    background = Color.White,
    surface = VeryLightBlue,
    onSurface = Color.Black,
    error = DarkRed,
)

private val DarkThemeColors = darkColors(
    primary = Blue,
    onPrimary = Color.White,
    primaryVariant = Blue,
    secondary = SkyBlue,
    onSecondary = Color.White,
    background = DarkestBlue,
    surface = Gray,
    onSurface = Color.White,
    error = DarkRed,
)
