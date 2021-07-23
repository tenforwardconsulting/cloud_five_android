package com.cloudfiveapp.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.cloudfiveapp.android.ui.stubs.showcase.ThemeShowcase
import com.cloudfiveapp.android.ui.theme.CloudFiveTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Preview(showSystemUi = true)
@Composable
fun CloudFiveApp() {
    CloudFiveTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(Color(0x55000000), darkIcons = false)
            }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                // RandomClouds(
                //     modifier = Modifier.systemBarsPadding()
                // )
                ThemeShowcase(
                    modifier = Modifier.systemBarsPadding()
                )
            }
        }
    }
}
