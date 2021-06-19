package io.github.wparks.androidApp.ui.park

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.wparks.shared.Asset

@Composable
fun AssetCard(asset: Asset,
              modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 60.dp)
        .padding(horizontal = 8.dp, vertical = 5.dp)
        .clip(RoundedCornerShape(8.dp)),
        backgroundColor = Color(0xFFEBEBEB),
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        ) {
            Row(modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetIcon(asset = asset)
                Spacer(modifier = Modifier.size(width = 10.dp, height = 0.dp))
                Column {
                    Text(text = asset.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black)
                    asset.subtype?.let {
                        Text(text = it, fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }

            asset.size?.let {
                Text(modifier = Modifier
                    .padding(10.dp, 0.dp)
                    .align(Alignment.CenterEnd),
                    text = it,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Composable
fun AssetIcon(asset: Asset) {
    val imageIcon = when (asset.typeId.toInt()) {
        100 -> Icons.Rounded.EventSeat
        101 -> Icons.Rounded.Sailing
        102 -> Icons.Rounded.PedalBike
        103 -> Icons.Rounded.Place
        104 -> Icons.Rounded.OutdoorGrill
        105 -> Icons.Rounded.Roofing
        106 -> Icons.Rounded.Sledding
        107 -> Icons.Rounded.LocalParking
        108 -> Icons.Rounded.Park
        109 -> Icons.Rounded.IceSkating
        110 -> Icons.Rounded.Skateboarding
        111 -> Icons.Rounded.Pets
        112 -> Icons.Rounded.Park
        113 -> Icons.Rounded.Park
        114 -> Icons.Rounded.Park
        115 -> Icons.Rounded.FitnessCenter
        116 -> Icons.Rounded.DownhillSkiing
        117 -> Icons.Rounded.SportsSoccer
        118 -> Icons.Rounded.SportsFootball
        119 -> Icons.Rounded.SportsBaseball
        120 -> Icons.Rounded.SportsRugby
        121 -> Icons.Rounded.Circle
        122 -> Icons.Rounded.Sports
        123 -> Icons.Rounded.Sports
        124 -> Icons.Rounded.Sports
        125 -> Icons.Rounded.SportsCricket
        126 -> Icons.Rounded.Sports
        127 -> Icons.Rounded.SportsHandball
        128 -> Icons.Rounded.SportsBasketball
        129 -> Icons.Rounded.SportsTennis
        130 -> Icons.Rounded.SportsTennis
        131 -> Icons.Rounded.SportsVolleyball
        132 -> Icons.Rounded.SportsFootball
        133 -> Icons.Rounded.Sports
        134 -> Icons.Rounded.DirectionsRun
        135 -> Icons.Rounded.Sports
        136 -> Icons.Rounded.Sports
        else -> Icons.Rounded.Park
    }
    Icon(imageIcon, asset.title)
}

@Preview("Asset item preview")
@Composable
fun AssetCardPreview() {
    Column {
        AssetCard(asset = Asset(title = "Seating", subtype = "BENCH", size = "Mini", id = 0, typeId = 0, parkId = 0, latitude = 0.0, longitude = 0.0))
        AssetCard(asset = Asset(title = "Seating", subtype = null, size = "Mini", id = 0, typeId = 0, parkId = 0, latitude = 0.0, longitude = 0.0))
    }
}