package com.example.massmanager.Dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.massmanager.ViewModel.SignUpViewModel
import com.example.massmanager.R

@Composable
fun UserScreen(navController: NavController, viewModel: SignUpViewModel) {

    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current
    val message by viewModel.message.collectAsState()

    val scrollState = rememberScrollState()

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            userName = ""
            email = ""
            phone = ""
            password = ""
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    // ✨ ওল্ড ও নিউ সব কমপোজ ভার্সনের জন্য ইউনিভার্সাল এরর-ফ্রি টেক্সটফিল্ড কালারস
    val customTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White.copy(alpha = 0.03f),
        unfocusedContainerColor = Color.Transparent,
        focusedBorderColor = colorResource(R.color.teal_700),
        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
        focusedLabelColor = colorResource(R.color.teal_700),
        unfocusedLabelColor = Color.Gray,
        focusedLeadingIconColor = colorResource(R.color.teal_700),
        unfocusedLeadingIconColor = Color.Gray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background)) // মেন ডার্ক ব্যাকগ্রাউন্ড
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 🌟 Top Interactive Header Icon
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(colorResource(R.color.teal_700).copy(alpha = 0.15f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_person_24),
                contentDescription = "User Icon",
                tint = colorResource(R.color.teal_700),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📝 Title & Subtitle
        Text(
            text = "Create New User",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Fill up the credentials to register a member",
            fontSize = 13.sp,
            color = Color.Black.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🚨 Premium Moving Notice Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF5252).copy(alpha = 0.08f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚠️ নতুন ইউজার নিবন্ধন শুধুমাত্র অ্যাডমিনের মাধ্যমে করা যাবে। অ্যাকাউন্টের জন্য অ্যাডমিনের সাথে যোগাযোগ করুন, ধন্যবাদ।",
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(),
                    color = Color(0xFFEF2121),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📥 Main Form Glass-Container Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.02f) // সেমি ট্রান্সপারেন্ট প্রিমিয়াম লুক
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                // ১. User Name
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text(
                            "User Name",
                            color = Color(0xFF2E7D32)
                        )
                    },

                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_person_24),
                            contentDescription = "User",
                            tint = Color(0xFF2E7D32)
                        )
                    },

                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1B5E20),
                        unfocusedTextColor = Color(0xFF1B5E20),

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color(0xFF2E7D32),
                        unfocusedIndicatorColor = Color(0xFFBDBDBD),

                        cursorColor = Color(0xFF2E7D32)
                    )
                )

                // ২. Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email Address") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_email_24),
                            contentDescription = "Email"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1B5E20),
                        unfocusedTextColor = Color(0xFF1B5E20),

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color(0xFF2E7D32),
                        unfocusedIndicatorColor = Color(0xFFBDBDBD),

                        cursorColor = Color(0xFF2E7D32)
                    ),
                    singleLine = true
                )



                // ৩. Phone Number
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Phone Number") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_settings_phone_24),
                            contentDescription = "Phone"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1B5E20),
                        unfocusedTextColor = Color(0xFF1B5E20),

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color(0xFF2E7D32),
                        unfocusedIndicatorColor = Color(0xFFBDBDBD),

                        cursorColor = Color(0xFF2E7D32)
                    ),
                    singleLine = true
                )

                // ৪. Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_password_24),
                            contentDescription = "Password"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1B5E20),
                        unfocusedTextColor = Color(0xFF1B5E20),

                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color(0xFF2E7D32),
                        unfocusedIndicatorColor = Color(0xFFBDBDBD),

                        cursorColor = Color(0xFF2E7D32)
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 🚀 Premium Futuristic Button
        Button(
            onClick = {
                if (userName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Fill in the blank fields", Toast.LENGTH_LONG).show()
                } else {

                    val sessionManager = SessionManager(context)

                    val role = sessionManager.getRole()
                    val isAdmin = role == "admin"

//                    val sessionManager = SessionManager(context)
//                    val adminId = sessionManager.adminId()
//                    val currentUserId = sessionManager.getUserId()
//                    val isAdmin = adminId == currentUserId

                    if (!isAdmin) {
                        Toast.makeText(context, "Only admin can create account", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    viewModel.signup(userName, email, phone, password, sessionManager.getUserId())
                }
            },
            enabled = !loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.teal_700),
                disabledContainerColor = colorResource(R.color.teal_700).copy(alpha = 0.4f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Create User",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun Previofile() {
    UserScreen(rememberNavController(), viewModel())
}