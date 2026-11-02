package com.example.loginapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LoginColors = lightColorScheme(
    primary = Purple,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = PurpleDark,
    background = Background,
    surface = androidx.compose.ui.graphics.Color.White
)

@Composable
fun LoginAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LoginColors, content = content)
}
