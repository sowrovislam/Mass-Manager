package com.example.massmanager.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.R

@Composable
fun ProfileScreen(navController: NavController) {

    // 🔥 Sample user data (later connect API / DB)
    var name by remember { mutableStateOf("Masum") }
    var email by remember { mutableStateOf("masum@gmail.com") }
    var number by remember { mutableStateOf("+880123456789") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .padding(16.dp)
    ) {

        // ================= TOP USER INFO =================

        Spacer(modifier = Modifier.height(40.dp))

        // ================= CARD VIEW =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.primaryColor))
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text("Admin Information", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(12.dp))

                RowItem("📧 Email", email)
                RowItem("📱 Number", number)
                RowItem("👤 Name", name)

                Spacer(modifier = Modifier.height(12.dp))

                Divider()

                Spacer(modifier = Modifier.height(12.dp))

            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ================= LOGOUT BUTTON =================
        Button(
            onClick = {
                // 👉 Logout logic here
                // navController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Logout", color = Color.White)
        }
    }
}

// 🔥 Small reusable row (still inside same file)
@Composable
fun RowItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontWeight = FontWeight.Medium)
        Text(value, color = Color.Gray)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfile() {
    ProfileScreen(rememberNavController())
}