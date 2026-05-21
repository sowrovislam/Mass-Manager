package com.example.massmanager.Login_File

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }

    // 🔥 Smooth animation
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(1000),
        label = ""
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000),
        label = ""
    )

    // 🔥 Navigation handled safely
    LaunchedEffect(Unit) {

        startAnimation = true

        delay(1500)

        val sessionManager = SessionManager(context)

        if (sessionManager.isLoggedIn()) {
            navController.navigate(Screen.dashboard.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    // 🔥 UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // safe background
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.mass_manager),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .scale(scale)
                .alpha(alpha)
        )
    }
}