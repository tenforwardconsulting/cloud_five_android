package com.cloudfiveapp.android.ui.stubs.showcase

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cloudfiveapp.android.ui.theme.CloudFiveTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.toPaddingValues

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ThemeShowcase(
    modifier: Modifier = Modifier,
) {
    CloudFiveTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = { Text("Widen Collective") },
                        navigationIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add something")
                    }
                }
            ) {
                FlavorsList(
                    flavors = FLAVORS
                )
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
private val FLAVORS = buildList {
    repeat(50) {
        add(Flavor("Widen Collective", "v1.2.10", 77))
        add(Flavor("Widen Dev", "v1.2.10", 32))
    }
}

@Composable
private fun FlavorsList(
    @Suppress("SameParameterValue") flavors: List<Flavor>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues()
    ) {
        items(flavors) { flavor ->
            FlavorCard(flavor)
        }
    }
}

@Composable
private fun FlavorCard(
    flavor: Flavor,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(
                    text = flavor.name,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.weight(1f)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(flavor.version)
                    Text("#${flavor.buildNumber}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download APK for ${flavor.name}",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

private data class Flavor(
    val name: String,
    val version: String,
    val buildNumber: Int,
)
