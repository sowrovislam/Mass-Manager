package com.example.massmanager.Login_File

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.ViewModel.OtpViewModel
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R

@Composable
fun SignupScreen(navController: NavController, viewModel: OtpViewModel) {

    // Viewmodel States
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()
    val status by viewModel.status.collectAsState()
    val otp by viewModel.otpState.collectAsState()
    val status1 by viewModel.status1.collectAsState()

    // Context & UI Control States
    val context = LocalContext.current
    var isOtpSent by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Form Field States
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isSuccess = status == "OTP Verified Success"
    val isInvalid = status == "Invalid OTP"

    // Error Message Toast Trigger
    LaunchedEffect(message) {
        if (message != null) {
            when {

                message!!.contains("Success", ignoreCase = true) || message!!.contains(
                    "registered",
                    ignoreCase = true
                ) -> {
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()


                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }

                message!!.contains("exist", ignoreCase = true) || message!!.contains(
                    "fail",
                    ignoreCase = true
                ) -> {

                    Toast.makeText(context, "Registration Failed: $message", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .imePadding() // কিবোর্ড আসলে স্ক্রিন উপরে উঠে যাবে, ডিজাইন ভাঙবে না
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // ছোট স্ক্রিনের ডিভাইসেও স্ক্রোল হবে
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header Section
            Text(
                text = "Create Account",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.textColor)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please provide your signup information to access the dashboard.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields Container (Card Design)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.white),
                shadowElevation = 0.dp // ক্লিন মিনিমালিস্ট লুকের জন্য
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    // --- EMAIL FIELD WITH INTEGRATED OTP BUTTON ---
                    CustomInputLabel(text = "Email Address*")
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = false },
                        placeholder = { Text("name@example.com", color = Color.DarkGray) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = emailError,
                        singleLine = true,

                        // 🔥 এই লাইনটি যোগ করা হয়েছে: OTP পাঠানো হলে এটি True হবে এবং ফিল্ডটি লক হয়ে যাবে
                        readOnly = isOtpSent,

                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_email_24),
                                contentDescription = "Email",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = getModernTextFieldColors(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            // ওটিপি পাঠানো হয়ে গেলে ট্রেইলিং আইকন বা বাটনটি পুরোপুরি হাইড (Hide) করে দেওয়া হবে
                            if (!isOtpSent) {
                                Button(
                                    onClick = {
                                        emailError = email.isBlank()
                                        if (!emailError) {
                                            viewModel.sendOtp(email)
                                            isOtpSent = true
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(R.color.primaryColor)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Text(
                                        text = "Get OTP",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                // ওটিপি সাকসেসফুলি সেন্ট হলে বাটনের জায়গায় একটি সুন্দর গ্রীন টিক মার্ক (✓) দেখাবে
                                Icon(
                                    painter = painterResource(R.drawable.outline_check_small_24), // আপনার প্রোজেক্টে থাকা যেকোনো টিকমার্ক আইকন দিন
                                    contentDescription = "Sent",
                                    tint = Color(0xFF2E7D32), // Green Color
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    // --- CODE / OTP VERIFICATION ROW ---
                    CustomInputLabel(text = "Verification Code*")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = otp,
                            onValueChange = { viewModel.setOtp(it); otpError = false },
                            placeholder = { Text("000000", color = Color.DarkGray) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            isError = otpError,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_password_24),
                                    contentDescription = "OTP",
                                    tint = colorResource(R.color.primaryColor)
                                )
                            },
                            colors = getModernTextFieldColors(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                emailError = email.isBlank()
                                otpError = otp.isBlank()
                                if (!emailError && !otpError) {
                                    viewModel.verifyOtp(email, otp)
                                }
                            },
                            enabled = !isSuccess,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(130.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    isSuccess -> Color(0xFF2E7D32)
                                    isInvalid -> Color(0xFFD32F2F)
                                    else -> colorResource(R.color.primaryColor)
                                },
                                disabledContainerColor = Color(0xFF2E7D32)
                            )
                        ) {
                            Text(
                                text = when {
                                    isSuccess -> "Verified ✓"
                                    isInvalid -> "Retry ✕"
                                    else -> "Verify"
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- USERNAME FIELD ---
                    CustomInputLabel(text = "Username*")
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter Monitor Name", color = Color.DarkGray) },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.outline_person_24),
                                contentDescription = "User",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        colors = getModernTextFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- PHONE FIELD ---
                    CustomInputLabel(text = "Phone Number*")
                    OutlinedTextField(
                        value = number,
                        onValueChange = { number = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter Phone Number", color = Color.DarkGray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.outline_settings_phone_24),
                                contentDescription = "Phone",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        colors = getModernTextFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- PASSWORD FIELD ---
                    CustomInputLabel(text = "Password*")
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••", color = Color.DarkGray) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_password_24),
                                contentDescription = "Password",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        trailingIcon = {
//                            val iconPainter = if (passwordVisible) {
//                                painterResource(R.drawable.baseline_password_24) // আপনার ড্রয়েবল আইকনের নাম দিন
//                            } else {
//                                painterResource(R.drawable.outline_person_24) // আপনার ড্রয়েবল আইকনের নাম দিন
//                            }
//
//                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                                Icon(
//                                    painter = iconPainter, // এখানে imageVector এর বদলে painter হবে
//                                    contentDescription = "Toggle Password Visibility"
//                                )
//                            }
                        },
                        colors = getModernTextFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SIGN UP / SUBMIT BUTTON ---
            Button(
                onClick = {
                    val nameError = name.isBlank()
                    val mailError = email.isBlank()
                    val numError = number.isBlank()
                    val passError = password.isBlank()

                    if (nameError || mailError || numError || passError) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (status.equals("OTP Verified Success", ignoreCase = true)) {
                        // শুধুমাত্র ফাংশনটি কল করুন, এখানে নেভিগেট করবেন না
                        viewModel.register(name, email, number, password)
                    } else {
                        Toast.makeText(context, "Please verify OTP first", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primaryColor),
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Loading Overlay (ফুলস্ক্রিন ব্লকিং প্রোগ্রেস বার - আধুনিক অ্যাপ স্ট্যান্ডার্ড)
        AnimatedVisibility(
            visible = loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier.size(80.dp),
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(R.color.primaryColor))
                    }
                }
            }
        }
    }
}

// ইনপুট লেবেলের জন্য রিইউজেবল কম্পোনেন্ট
@Composable
fun CustomInputLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.textColor),
        modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
    )
}

// মডার্ন টেক্সট ফিল্ড কালার স্কিম জেনারেটর
@Composable
fun getModernTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    cursorColor = colorResource(R.color.primaryColor),
    focusedContainerColor = Color(0xFFF9F9FA),
    unfocusedContainerColor = Color(0xFFF9F9FA),
    errorContainerColor = Color(0xFFFFF5F5),
    focusedBorderColor = colorResource(R.color.primaryColor),
    unfocusedBorderColor = Color(0xFFE2E8F0),
    errorBorderColor = Color.Red
)

@Preview(showSystemUi = true)
@Composable
fun signupui() {
    val navController = rememberNavController()
    // মক ভিউমডেল পাস করা হয়েছে প্রিভিউয়ের জন্য
    val viewModel = OtpViewModel()
    SignupScreen(navController = navController, viewModel = viewModel)
}