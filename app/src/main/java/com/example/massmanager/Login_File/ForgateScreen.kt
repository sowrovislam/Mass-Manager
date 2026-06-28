package com.example.massmanager.Login_File

// 🔥 FULL IMPORTS
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.massmanager.R
import com.example.massmanager.ViewModel.OtpViewModel

// 🔥 STEP ENUM
enum class ForgotPasswordStep {
    SEND_EMAIL, VERIFY_OTP, UPDATE_PASSWORD
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgateScreen(
    navController: NavController,
    viewModel: OtpViewModel= viewModel()
) {

    var step by remember { mutableStateOf(ForgotPasswordStep.SEND_EMAIL) }
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // 🔥 STATE FROM VIEWMODEL
    val status by viewModel.status.collectAsState()
    val status1 by viewModel.status1.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Forgot Password", fontWeight = FontWeight.Bold)
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

                // 🔹 STEP 1: SEND OTP
                AnimatedVisibility(step == ForgotPasswordStep.SEND_EMAIL) {
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (email.isNotBlank()) {
                                    viewModel.sendOtp(email)
                                    step = ForgotPasswordStep.VERIFY_OTP
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Send OTP")
                        }
                    }
                }

                // 🔹 STEP 2: VERIFY OTP
                AnimatedVisibility(step == ForgotPasswordStep.VERIFY_OTP) {
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (otp.length == 6) {
                                    viewModel.verifyOtp(email, otp)
                                    step = ForgotPasswordStep.UPDATE_PASSWORD
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Verify OTP")
                        }
                    }
                }

                // 🔹 STEP 3: RESET PASSWORD
                AnimatedVisibility(step == ForgotPasswordStep.UPDATE_PASSWORD) {
                    Column {

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("New Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (password.isNotBlank() && password == confirmPassword) {
                                    viewModel.resetPassword(email, password) {
                                        navController.popBackStack()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Update Password")
                        }
                    }
                }

                // 🔥 LOADING
                if (loading) {
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator()
                }

                // 🔥 STATUS MESSAGE
                if (status1.isNotEmpty()) {
                    Text(status1, color = MaterialTheme.colorScheme.primary)
                }

                if (status.isNotEmpty()) {
                    Text(status, color = MaterialTheme.colorScheme.primary)
                }

                if (message.isNotEmpty()) {
                    Text(message, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}