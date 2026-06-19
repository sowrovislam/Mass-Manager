package com.example.massmanager.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.R

// USER MODEL
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val number: String,
    val password: String
)

@Composable
fun ProfileScreen(navController: NavController, isAdmin: Boolean = true) {

    // Admin data
    val adminName = "Admin"
    val adminEmail = "admin@gmail.com"
    val adminNumber = "+880123456789"

    // Users list
    var users by remember {
        mutableStateOf(
            listOf(
                User(1, "Rahim", "rahim@gmail.com", "01711111111", "1234"),
                User(2, "Karim", "karim@gmail.com", "01822222222", "1234"),
                User(3, "Sumi", "sumi@gmail.com", "01933333333", "1234")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // ================= ADMIN CARD =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.primaryColor)
            )
        ) {

            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Admin Profile",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Name: $adminName", color = Color.White)
                Text("Email: $adminEmail", color = Color.White)
                Text("Number: $adminNumber", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ================= USERS LIST =================
        Text(
            text = "All Users",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(users) { user ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(user.name, fontWeight = FontWeight.Bold)
                        Text(user.email, color = Color.Gray)
                        Text(user.number, color = Color.Gray)

                        Spacer(modifier = Modifier.height(10.dp))

                        // ADMIN ONLY BUTTONS
                        if (isAdmin) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Button(
                                    onClick = {
                                        // UPDATE LOGIC
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue
                                    )
                                ) {
                                    Text("Update")
                                }

                                Button(
                                    onClick = {
                                        users = users.filter { it.id != user.id }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    )
                                ) {
                                    Text("Delete")
                                }
                            }

                        } else {
                            Text(
                                text = "View Only",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ================= LOGOUT =================
        Button(
            onClick = {
                navController.popBackStack()
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
@Preview(showSystemUi = true)
@Composable
fun PreviewProfile() {
    ProfileScreen(rememberNavController())
}