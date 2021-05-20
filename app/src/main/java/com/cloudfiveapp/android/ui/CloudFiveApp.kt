package com.cloudfiveapp.android.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cloudfiveapp.android.R
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import kotlin.random.Random

@Preview
@Composable
fun CloudFiveApp() {
    ProvideWindowInsets {
        MaterialTheme {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.sky_blue))
                    .systemBarsPadding()
            ) {
                Box {
                    val clouds = remember { mutableStateOf(createRandomClouds()) }
                    NiceClouds(clouds.value)

                    TextButton(
                        onClick = {
                            clouds.value = createRandomClouds()
                        },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        Text("Refresh Clouds")
                    }
                }
            }
        }
    }
}

private val DRAWABLE_IDS = listOf(
    R.drawable.cloud1,
    R.drawable.cloud2,
    R.drawable.cloud3,
    R.drawable.cloud4,
    R.drawable.cloud5,
    R.drawable.cloud6,
    R.drawable.cloud7,
)

private val SIZES = listOf(70.dp, 80.dp, 100.dp, 120.dp, 150.dp)

data class Cloud(
    @DrawableRes val drawableRes: Int,
    val size: Dp,
)

private fun createRandomClouds() =
    DRAWABLE_IDS.map { drawableResId ->
        Cloud(drawableResId, SIZES.random())
    }

@Composable
fun NiceClouds(
    clouds: List<Cloud>,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.sky_blue))
    ) {
        clouds.forEach { cloud ->
            val x = with(LocalDensity.current) {
                Random.nextInt(constraints.maxWidth - cloud.size.toPx().toInt()).toDp()
            }
            val y = with(LocalDensity.current) {
                Random.nextInt(constraints.maxHeight - cloud.size.toPx().toInt()).toDp()
            }

            Image(
                painter = painterResource(id = cloud.drawableRes),
                contentDescription = null,
                modifier = Modifier
                    .offset(x, y)
                    .size(cloud.size)
            )
        }
        Text(
            text = "Hello Cloud Five!",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
