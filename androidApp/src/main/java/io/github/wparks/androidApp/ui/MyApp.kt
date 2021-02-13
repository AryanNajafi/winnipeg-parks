package io.github.wparks.androidApp.ui

import androidx.compose.runtime.Composable
import io.github.wparks.androidApp.ui.theme.AppTheme

@Composable
fun MyApp(content: @Composable () -> Unit) {
    AppTheme(darkTheme = false) {
        content()
    }
}