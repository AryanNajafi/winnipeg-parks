package io.github.wparks.androidApp.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

@Stable
class Filter(val id: Int, val title: String, checked: Boolean = false) {
    var checked = mutableStateOf(checked)
}