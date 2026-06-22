package com.example.massmanager.Login_File

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Smooth scale animation (0.7f to 1.0f)
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = tween(durationMillis = 1000), // ১ সেকেন্ড ধরে লোগো বড় হবে
        label = "LogoScale"
    )

    // Smooth fade-in animation (0f to 1.0f)
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800), // ৮০০ মিলিসেকেন্ডে লোগো ভেসে উঠবে
        label = "LogoAlpha"
    )

    // ১. অ্যাপ ওপেন হওয়া মাত্রই অ্যানিমেশন এবং নেটওয়ার্ক মনিটর চালু হবে (কোনো দেরি ছাড়া)
    LaunchedEffect(Unit) {
        startAnimation = true
        networkMonitor.start()
    }

    // ২. নেভিগেশন এবং ইন্টারনেটের লজিক আলাদা রাখা হয়েছে যেন লোগো দ্রুত শো করে
    LaunchedEffect(isConnected) {
        // লোগো দেখার জন্য ব্যাকগ্রাউন্ডে ২ সেকেন্ড অপেক্ষা করবে
        delay(1000)

        if (!isConnected) {
            showDialog = true
        } else {
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
    }

    // No Internet Connection Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("No Internet ❌") },
            text = { Text("Please turn on your internet connection to continue.") },
            confirmButton = {
                TextButton(onClick = { activity.finish() }) {
                    Text("Exit")
                }
            }
        )
    }

    // Main UI Content Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {

        // 🎯 LOGO & APP BRANDING CENTER BLOCK
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_mess_manager_logo),
                contentDescription = "Mess Manager Logo",
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mess Manager",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Developed by Md Sowrov Islam",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // ⏳ BOTTOM LOADING PROGRESS BAR BLOCK
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .width(140.dp)
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Loading workspace...",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
    }
}