package com.example.massmanager.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Login_File.LoginScreen
import com.example.massmanager.Login_File.SplashScreen

@Composable
fun NavigationUI() {

    val isLoggedIn = true
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Splash.route else Screen.Login.route
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Home.route) {
//            HomeScreen(navController)
        }
    }
}
    sealed class Screen(val route: String) {
        object Splash : Screen("SplashScreen")
        object Login : Screen("LoginScreen")
        object Home : Screen("HomeScreen")
    }