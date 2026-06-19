package com.example.massmanager.Login_File

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.massmanager.R

// Step Enum
enum class ForgotPasswordStep {
    SEND_EMAIL, VERIFY_OTP, UPDATE_PASSWORD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgateScreen(navController: NavController) {

    var step by remember { mutableStateOf(ForgotPasswordStep.SEND_EMAIL) }
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Forgot Password",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                // ICON
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    modifier = Modifier.size(90.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = when (step) {
                                ForgotPasswordStep.SEND_EMAIL ->
                                    painterResource(R.drawable.baseline_email_24)

                                ForgotPasswordStep.VERIFY_OTP ->
                                    painterResource(R.drawable.baseline_vpn_key_24)

                                ForgotPasswordStep.UPDATE_PASSWORD ->
                                    painterResource(R.drawable.baseline_lock_24)
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // TITLE
                Text(
                    text = when (step) {
                        ForgotPasswordStep.SEND_EMAIL -> "Reset Password"
                        ForgotPasswordStep.VERIFY_OTP -> "Verify OTP"
                        ForgotPasswordStep.UPDATE_PASSWORD -> "New Password"
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // DESCRIPTION
                Text(
                    text = when (step) {
                        ForgotPasswordStep.SEND_EMAIL ->
                            "Enter your email to get OTP"

                        ForgotPasswordStep.VERIFY_OTP ->
                            "Enter OTP sent to: $email"

                        ForgotPasswordStep.UPDATE_PASSWORD ->
                            "Create a strong password"
                    },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // STEP 1
                AnimatedVisibility(
                    visible = step == ForgotPasswordStep.SEND_EMAIL,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_email_24),
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (email.isNotBlank()) {
                                    step = ForgotPasswordStep.VERIFY_OTP
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Send OTP", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // STEP 2
                AnimatedVisibility(
                    visible = step == ForgotPasswordStep.VERIFY_OTP,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {

                        OutlinedTextField(
                            value = otp,
                            onValueChange = { if (it.length <= 6) otp = it },
                            label = { Text("OTP Code") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_vpn_key_24),
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (otp.length == 6) {
                                    step = ForgotPasswordStep.UPDATE_PASSWORD
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Verify OTP", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // STEP 3
                AnimatedVisibility(
                    visible = step == ForgotPasswordStep.UPDATE_PASSWORD,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("New Password") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_lock_24),
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_lock_24),
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (password.isNotBlank() &&
                                    password == confirmPassword
                                ) {
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Update Password", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}