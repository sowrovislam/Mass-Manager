package com.example.massmanager.Navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Dashboard.BazarShdule
import com.example.massmanager.Dashboard.DailyMeal
import com.example.massmanager.Dashboard.GroceryListScreen

import com.example.massmanager.ViewModel.LoginViewModel
import com.example.massmanager.ViewModel.OtpViewModel
import com.example.massmanager.ViewModel.SignUpViewModel
import com.example.massmanager.Dashboard.ProfileScreen
import com.example.massmanager.Dashboard.UserScreen
import com.example.massmanager.Dashboard.dashboardScreen
import com.example.massmanager.Login_File.ForgateScreen
import com.example.massmanager.Login_File.LoginScreen
import com.example.massmanager.Login_File.SignupScreen
import com.example.massmanager.Login_File.SplashScreen
import com.example.massmanager.ViewModel.GroceryViewModel
import com.example.massmanager.ViewModel.MealViewModel
import com.example.massmanager.ViewModel.ScheduleViewModel
import com.example.massmanager.ViewModel.UserViewModel
import com.example.massmanager.ui.components.MealsData

@Composable
fun NavigationUI(context: Context) {


    val pref = context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)
    val isLoggedIn = pref.getBoolean("isLoggedIn", false)
    val start = if (isLoggedIn) Screen.Splash.route else Screen.Login.route




    val navController = rememberNavController()
    val viewModel: OtpViewModel= viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            val viewModel: LoginViewModel=viewModel()
            LoginScreen(navController, viewModel)
        }

        composable(Screen.Signup.route) {
            val viewModel: OtpViewModel = viewModel()
            SignupScreen(navController,viewModel)
        }


        composable(Screen.Forgate.route) {
            ForgateScreen(navController)
        }



        composable(Screen.dashboard.route) {

            val viewModel: ScheduleViewModel= viewModel()
            dashboardScreen(navController,viewModel)
        }

        composable(Screen.Profile.route) {
            val viewModel: UserViewModel= viewModel()
            ProfileScreen(navController,viewModel)
        }


    composable(Screen.Users.route) {

        val viewModel: SignUpViewModel = viewModel()

        val otpViewModel: OtpViewModel = viewModel()
        UserScreen(navController,viewModel,otpViewModel)
    }
    composable(Screen.Shdule.route) {

        val viewModel: ScheduleViewModel = viewModel()
        BazarShdule(navController,viewModel)
    }
    composable(Screen.Meal.route) {

        val viewModel: MealViewModel = viewModel()
        DailyMeal(navController,viewModel)
    }
        composable(Screen.MealsData.route) {

        val viewModel: ScheduleViewModel = viewModel()
            MealsData(navController,viewModel)
    }
        composable(Screen.GroceryListScreen.route) {

        val viewModel: GroceryViewModel = viewModel()
            GroceryListScreen(navController,viewModel)
    }


}
}
    sealed class Screen(val route: String) {
        object Splash : Screen("SplashScreen")
        object Login : Screen("LoginScreen")

        object Signup: Screen("SignupScreen")

        object Forgate: Screen("ForgateScreen")

        object dashboard: Screen("dashboardScreen")

        object Profile: Screen("ProfileScreen")
        object Users: Screen("UserScreen")

        object Shdule: Screen("BazarShdule")

        object Meal: Screen("DailyMeal")

        object MealsData: Screen("MealsData")

        object GroceryListScreen: Screen("GroceryListScreen")
    }