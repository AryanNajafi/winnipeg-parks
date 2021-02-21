package io.github.wparks.androidApp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.wparks.shared.Park

@Composable
fun ParkCard(park: Park,
             onItemClick: (Park) -> Unit,
             modifier: Modifier = Modifier) {
    Card(modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
            .clickable {
                onItemClick(park)
            },
    ) {
        Column(modifier = Modifier.padding(16.dp, 10.dp)) {
            Text(text = park.title, style = MaterialTheme.typography.h6)
            Text(text = park.address, style = MaterialTheme.typography.body2)
        }
    }
}