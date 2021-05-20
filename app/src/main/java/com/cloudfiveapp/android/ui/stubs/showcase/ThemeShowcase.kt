package com.cloudfiveapp.android.ui.stubs.showcase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.toPaddingValues

@Preview(showBackground = true, backgroundColor = 4284460768)
@Composable
fun ThemeShowcase(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        FlavorsList(
            flavors = FLAVORS
        )
    }
}

@OptIn(ExperimentalStdlibApi::class)
private val FLAVORS = buildList {
    repeat(100) {
        add(Flavor("Widen Collective", "v1.2.10", 77))
        add(Flavor("Widen Dev", "v1.2.10", 32))
    }
}

@Composable
private fun FlavorsList(
    @Suppress("SameParameterValue") flavors: List<Flavor>,
    onFlavorClick: (Flavor) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues()
    ) {
        items(flavors) { flavor ->
            FlavorCard(flavor, onFlavorClick)
        }
    }
}

@Composable
private fun FlavorCard(
    flavor: Flavor,
    onFlavorClick: (Flavor) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable(onClick = { onFlavorClick(flavor) }),
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
            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

private data class Flavor(
    val name: String,
    val version: String,
    val buildNumber: Int,
)
