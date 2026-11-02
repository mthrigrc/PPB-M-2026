package com.example.loginapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loginapp.ui.theme.Error
import com.example.loginapp.ui.theme.Purple
import com.example.loginapp.ui.theme.Success
import com.example.loginapp.viewmodel.LoginResult
import com.example.loginapp.viewmodel.LoginUiState

@Composable
fun LoginScreen(
    state: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onLogout: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val loggedIn = state.result is LoginResult.Success

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFEDE7FF), Color(0xFFF8F9FF), Color.White))
        ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Brush.linearGradient(listOf(Purple, Color(0xFF8057E8))), RoundedCornerShape(50))
                        .padding(17.dp)
                ) {
                    Icon(
                        imageVector = if (loggedIn) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.height(38.dp)
                    )
                }
                Spacer(Modifier.height(18.dp))
                Text("User Login", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color(0xFF171525))
                Spacer(Modifier.height(7.dp))
                Text(
                    if (loggedIn) "Kamu berhasil masuk ke aplikasi." else "Silakan masukkan username\ndan password",
                    color = Color(0xFF666477), textAlign = TextAlign.Center, lineHeight = 21.sp
                )
                Spacer(Modifier.height(26.dp))

                if (loggedIn) {
                    Text("Selamat datang, ${(state.result as LoginResult.Success).username}!", color = Success, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = onLogout, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp)) {
                        Text("LOGOUT", fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedTextField(
                        value = state.username,
                        onValueChange = onUsernameChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Username") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Purple, focusedLabelColor = Purple)
                    )
                    Spacer(Modifier.height(14.dp))
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password")
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Purple, focusedLabelColor = Purple)
                    )
                    Spacer(Modifier.height(22.dp))
                    Button(
                        onClick = onLogin,
                        enabled = state.result !is LoginResult.Loading,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Purple)
                    ) {
                        if (state.result is LoginResult.Loading) CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.height(22.dp))
                        else Text("LOGIN", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                    when (val result = state.result) {
                        is LoginResult.Error -> {
                            Spacer(Modifier.height(16.dp))
                            Text(result.message, color = Error, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
                        }
                        else -> Unit
                    }
                    Spacer(Modifier.height(22.dp))
                    HorizontalDivider(color = Color(0xFFE9E7F0))
                    Spacer(Modifier.height(14.dp))
                    Text("Akun dummy untuk testing", color = Color.Gray, fontSize = 12.sp)
                    Spacer(Modifier.height(5.dp))
                    Text("admin  /  12345", color = Purple, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
