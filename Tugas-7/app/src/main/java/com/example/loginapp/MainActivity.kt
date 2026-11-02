package com.example.loginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loginapp.data.local.AppDatabase
import com.example.loginapp.data.repository.UserRepository
import com.example.loginapp.ui.LoginScreen
import com.example.loginapp.ui.theme.LoginAppTheme
import com.example.loginapp.viewmodel.LoginViewModel
import com.example.loginapp.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(applicationContext).userDao()
        val repository = UserRepository(dao)

        setContent {
            LoginAppTheme {
                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(repository)
                )
                val state = loginViewModel.uiState.collectAsStateWithLifecycle()

                LoginScreen(
                    state = state.value,
                    onUsernameChange = loginViewModel::onUsernameChange,
                    onPasswordChange = loginViewModel::onPasswordChange,
                    onLogin = loginViewModel::login,
                    onLogout = loginViewModel::logout
                )
            }
        }
    }
}
