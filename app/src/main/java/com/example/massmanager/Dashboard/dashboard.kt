package com.example.massmanager.Dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Navigation.Screen

@Composable
fun dashboardScreen(navController: NavController){

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val context = LocalContext.current
        val session = SessionManager(context)

        Spacer(modifier = Modifier.height(80.dp))
        Button(

            onClick = {

                // Logout
                val sessionManager = SessionManager(context)
                sessionManager.logout()

                // Go Login
                navController.navigate(Screen.Login) {
                    popUpTo("dashboard") { inclusive = true }
                }
            }

        ) {

            Text(text = "Logout")
        }
        Text("I Love YOu ")
    }












}