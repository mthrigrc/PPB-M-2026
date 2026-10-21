package com.example.libraryreg.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF1E5FD8)
val Ink = Color(0xFF14213D)
val Muted = Color(0xFF5B6577)
val Line = Color(0xFF8E9BB5)
val CardBorder = Color(0xFFDDE4F0)
val Danger = Color(0xFFC62828)
val Green = Color(0xFF2E7D4F)
val Orange = Color(0xFFB25E00)
val Purple = Color(0xFF6A4BC4)
val Bg = Color(0xFFF4F7FC)

private val Colors = lightColorScheme(
    primary = Blue, onPrimary = Color.White,
    background = Bg, onBackground = Ink,
    surface = Color.White, onSurface = Ink,
    error = Danger, outline = Line
)

@Composable
fun LibraryRegTheme(content: @Composable () -> Unit) =
    MaterialTheme(colorScheme = Colors, content = content)
