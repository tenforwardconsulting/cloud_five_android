package com.cloudfiveapp.android.ui.stubs.clouds

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cloudfiveapp.android.R
import kotlin.random.Random

@Composable
fun RandomClouds(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        val clouds = remember { mutableStateOf(createRandomClouds()) }
        NiceClouds(
            clouds.value,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
        )

        Button(
            onClick = {
                clouds.value = createRandomClouds()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Refresh Clouds")
        }
    }
}

@Composable
private fun NiceClouds(
    clouds: List<Cloud>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
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

private val DRAWABLE_IDS = listOf(
    R.drawable.cloud1,
    R.drawable.cloud2,
    R.drawable.cloud3,
    R.drawable.cloud4,
    R.drawable.cloud5,
    R.drawable.cloud6,
)

private val SIZES = listOf(70.dp, 80.dp, 100.dp, 120.dp, 150.dp)

private data class Cloud(
    @DrawableRes val drawableRes: Int,
    val size: Dp,
)

private fun createRandomClouds() =
    DRAWABLE_IDS.map { drawableResId ->
        Cloud(drawableResId, SIZES.random())
    }
