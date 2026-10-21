package com.example.libraryreg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.libraryreg.ui.*
import com.example.libraryreg.ui.theme.LibraryRegTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))
        setContent { LibraryRegTheme { LibraryRegApp() } }
    }
}

@Composable
fun LibraryRegApp() {
    val nav = rememberNavController()
    val vm: MemberViewModel = viewModel()
    val back: () -> Unit = { nav.popBackStack() }

    NavHost(navController = nav, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onRegister = { nav.navigate("form") },
                onList = { nav.navigate("list?focus=false") },
                onSearch = { nav.navigate("list?focus=true") },
                onManage = { nav.navigate("list?focus=false") }
            )
        }
        composable(
            "form?id={id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            FormScreen(vm, memberId = id.takeIf { it > 0 }, onBack = back)
        }
        composable(
            "list?focus={focus}",
            arguments = listOf(navArgument("focus") { type = NavType.BoolType; defaultValue = false })
        ) { entry ->
            MemberListScreen(
                vm, focusSearch = entry.arguments?.getBoolean("focus") ?: false,
                onBack = back,
                onAdd = { nav.navigate("form") },
                onDetail = { nav.navigate("detail/$it") }
            )
        }
        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            DetailScreen(
                vm, memberId = entry.arguments?.getInt("id") ?: 0,
                onBack = back, onEdit = { nav.navigate("form?id=$it") }
            )
        }
    }
}
