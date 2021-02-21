package io.github.wparks.androidApp.ui.park

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import io.github.wparks.shared.Asset

@Composable
fun AssetCard(asset: Asset,
              modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clip(RoundedCornerShape(8.dp)),
        backgroundColor = Color(0xFFEBEBEB),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(16.dp, 10.dp)) {
                Text(text = asset.title, style = MaterialTheme.typography.h6)
                asset.subtype?.let {
                    Text(text = it, style = MaterialTheme.typography.body2)
                }
            }
            asset.size?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(10.dp, 0.dp).align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }

    }
}

@Preview("Asset item preview")
@Composable
fun AssetCardPreview() {
    AssetCard(asset = Asset(title = "Seating", subtype = "Bench", size = "Mini", id = 0, typeId = 0, parkId = 0, latitude = 0.0, longitude = 0.0))
}