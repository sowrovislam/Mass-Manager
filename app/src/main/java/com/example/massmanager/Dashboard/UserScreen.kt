package com.example.massmanager.Dashboard

import android.widget.Toast
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Api_Otp.Data_Class.SignUpViewModel
import com.example.massmanager.R

@Composable
fun UserScreen(navController: NavController, viewModel: SignUpViewModel) {

    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bac)) // Applied dark background
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Top Title
        Text(
            text = "Create User",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.primaryColor) // Updated to white
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Access to some features requires admin approval.",
            fontSize = 13.sp,
            color = colorResource(R.color.primaryColor) // Updated to a cleaner dark-mode friendly red
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "নিবন্ধন বন্ধ করা হয়েছে " +
                    "নতুন অ্যাকাউন্ট শুধুমাত্র অ্যাডমিন তৈরি করতে পারবেন।" +
                    "অ্যাক্সেস পাওয়ার জন্য অনুগ্রহ করে অ্যাডমিনের সাথে যোগাযোগ করুন।",
            maxLines = 1,
            modifier = Modifier.basicMarquee(),
            color = Color.Red,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.padding(20.dp),


        ) {

            // User Name
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("User Name", color = colorResource(R.color.textColor)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_person_24),
                        contentDescription = "User",
                        tint = colorResource(R.color.textColor)
                    )
                },
                shape = RoundedCornerShape(16.dp),

            )

            Spacer(modifier = Modifier.height(12.dp)) // Slightly increased spacing for aesthetic breathing room

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email Address", color = colorResource(R.color.textColor)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_email_24),
                        contentDescription = "Email",
                        tint = colorResource(R.color.textColor)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                shape = RoundedCornerShape(16.dp),

            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone Number
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone Number", color = colorResource(R.color.textColor)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_settings_phone_24),
                        contentDescription = "Phone",
                        tint = colorResource(R.color.textColor)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                shape = RoundedCornerShape(16.dp),

            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password", color = colorResource(R.color.textColor)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_password_24),
                        contentDescription = "Password",
                        tint =colorResource(R.color.textColor)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(16.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorResource(R.color.textColor))
                }
            }

            // Create User Button
            Button(
                onClick = {
                    if (userName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Fill in the blank fields", Toast.LENGTH_LONG).show()
                    } else {
                        val sessionManager = SessionManager(context)
                        val userId = sessionManager.getUserId()

                        viewModel.signup(userName, email, phone, password, userId)
                        userName = ""
                        email = ""
                        phone = ""
                        password = ""
                    }
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(start = 20.dp, end = 20.dp)
                ,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.teal_700),
                    disabledContainerColor = colorResource(R.color.teal_700).copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = if (loading) "Creating..." else "Create User",
                    color = colorResource(R.color.textColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Previofile() {
    UserScreen(rememberNavController(), viewModel())
}