package com.example.massmanager.Navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.OtpViewModel
import com.example.massmanager.Login_File.ForgateScreen
import com.example.massmanager.Login_File.LoginScreen
import com.example.massmanager.Login_File.SignupScreen
import com.example.massmanager.Login_File.SplashScreen

@Composable
fun NavigationUI() {

    val isLoggedIn = true
    val navController = rememberNavController()
//    val viewModel: OtpViewModel= viewModel()

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

        composable(Screen.Signup.route) {
            val viewModel: OtpViewModel = viewModel()
            SignupScreen(navController,viewModel)
        }


        composable(Screen.Forgate.route) {
            ForgateScreen(navController)
        }
    }
}
    sealed class Screen(val route: String) {
        object Splash : Screen("SplashScreen")
        object Login : Screen("LoginScreen")

        object Signup: Screen("SignupScreen")

        object Forgate: Screen("ForgateScreen")
    }