package io.github.wparks.androidApp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.wparks.androidApp.data.Filter
import io.github.wparks.androidApp.ui.theme.pink300
import io.github.wparks.androidApp.ui.theme.pink800

@Composable
fun FilterChip(
    filter: Filter,
    modifier: Modifier = Modifier,
    onCheckedChange: ((Boolean) -> Unit)?,
    ) {
    val (checked) = filter.checked
    val backgroundColor by animateColorAsState(if (checked) pink800 else pink300)
    val textColor by animateColorAsState(if (checked) Color.White else Color.Black)

    Surface(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 3.dp),
        shape = MaterialTheme.shapes.small,
        elevation = 8.dp,
        color = backgroundColor
    ) {
        Row(modifier = modifier.toggleable(
            value = checked,
            onValueChange = {
                filter.checked.value = it
                if (onCheckedChange != null) {
                    onCheckedChange(it)
                }
            }
        )) {
            Text(text = filter.title, modifier = modifier.padding(5.dp), color = textColor)
        }
    }
}