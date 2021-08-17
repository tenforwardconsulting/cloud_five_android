package com.cloudfiveapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.cloudfiveapp.android.ui.CloudFiveApp
import com.cloudfiveapp.android.ui.theme.CloudFiveTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CloudFiveTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
                    val useDarkIcons = MaterialTheme.colors.isLight
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = useDarkIcons,
                        )
                    }

                    CloudFiveApp()
                }
            }
        }
    }
}
