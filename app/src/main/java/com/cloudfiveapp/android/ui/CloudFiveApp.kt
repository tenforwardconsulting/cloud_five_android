package com.cloudfiveapp.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cloudfiveapp.android.ui.clouds.RandomClouds
import com.cloudfiveapp.android.ui.theme.CloudFiveTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding

@Preview
@Composable
fun CloudFiveApp() {
    ProvideWindowInsets {
        CloudFiveTheme {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                RandomClouds(
                    modifier = Modifier.systemBarsPadding()
                )
            }
        }
    }
}
