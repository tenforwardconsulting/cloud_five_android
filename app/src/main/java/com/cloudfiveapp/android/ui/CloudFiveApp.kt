package com.cloudfiveapp.android.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CloudFiveApp() {
    MaterialTheme {
        Scaffold {
            Text("Hello Cloud Five!")
        }
    }
}
