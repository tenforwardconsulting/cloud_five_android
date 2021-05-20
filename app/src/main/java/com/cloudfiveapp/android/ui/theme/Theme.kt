package com.cloudfiveapp.android.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryVariant = SilverLining,
    background = SkyBlue,
    surface = Color.White,
    onSurface = Color.Black,
)

@Composable
fun CloudFiveTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = LightThemeColors,
        shapes = CloudFiveShapes,
        content = content,
    )
}
