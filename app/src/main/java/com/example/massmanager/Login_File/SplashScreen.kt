package com.example.massmanager.Login_File

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.NetworkMonitor
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    val activity = context as Activity

    val networkMonitor = remember { NetworkMonitor(context) }
    val isConnected by networkMonitor.isConnected.collectAsState()

    var startAnimation by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var navigated by remember { mutableStateOf(false) }

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

    // Start network + animation
    LaunchedEffect(Unit) {
        networkMonitor.start()
        startAnimation = true
    }

    // Main splash logic
    LaunchedEffect(isConnected) {

        if (navigated) return@LaunchedEffect

        delay(1200)

        if (!isConnected) {
            showDialog = true
            return@LaunchedEffect
        }

        val sessionManager = SessionManager(context)

        navigated = true

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

    // UI Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("No Internet ❌") },
            text = { Text("Please turn on internet connection") },
            confirmButton = {
                TextButton(onClick = { activity.finish() }) {
                    Text("Exit")
                }
            }
        )
    }

    // Splash UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mass_manager),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )
    }
}