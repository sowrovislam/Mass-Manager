package com.example.massmanager.Dashboard

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.example.massmanager.ViewModel.OtpViewModel // 🔥 ওটিপির জন্য ভিউমডেল ইমপোর্ট
import com.example.massmanager.R

@Composable
fun UserScreen(
    navController: NavController,
    viewModel: SignUpViewModel,
    otpViewModel: OtpViewModel // 🔥 ওটিপি ভিউমডেল প্যারামিটার অ্যাড করা হয়েছে
) {

    // Form Field States
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Viewmodel States
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()
    val context = LocalContext.current

    // 🔥 ওটিপি ভিউমডেল স্টেটস
    val otpLoading by otpViewModel.loading.collectAsState()
    val otpMessage by otpViewModel.message.collectAsState()
    val otpStatus by otpViewModel.status.collectAsState()
    val otpCode by otpViewModel.otpState.collectAsState()

    val scrollState = rememberScrollState()

    // এরর হ্যান্ডলিং এবং ওটিপি কন্ট্রোল স্টেট
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }

    val isOtpSuccess = otpStatus == "OTP Verified Success"
    val isOtpInvalid = otpStatus == "Invalid OTP"

    // সাইনআপ মেসেজ লিসেনার
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            userName = ""
            email = ""
            phone = ""
            password = ""
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    // ওটিপি মেসেজ টোস্ট লিসেনার
    LaunchedEffect(otpMessage) {
        if (!otpMessage.isNullOrEmpty()) {
            Toast.makeText(context, otpMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(9.dp))

            // 🌟 Top Interactive Header Icon
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(colorResource(R.color.primaryColor).copy(alpha = 0.1f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_person_24),
                    contentDescription = "User Icon",
                    tint = colorResource(R.color.primaryColor),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📝 Title & Subtitle
            Text(
                text = "Create New User",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.textColor)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Fill up the credentials and verify email to register a member.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                        text = "⚠️ নোটিশ: ",
                        color = Color(0xFFEF2121),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "নতুন ইউজার নিবন্ধন শুধুমাত্র অ্যাডমিনের মাধ্যমে করা যাবে। অ্যাকাউন্টের জন্য অ্যাডমিনের সাথে যোগাযোগ করুন, ধন্যবাদ।",
                        modifier = Modifier.basicMarquee(),
                        color = Color(0xFFEF2121),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 📥 Main Form Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.white)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    // --- ১. EMAIL FIELD WITH INTEGRATED OTP BUTTON ---
                    DashboardInputLabel(text = "Email Address*")
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = false },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("name@example.com", color = Color.DarkGray) },
                        isError = emailError,
                        readOnly = isOtpSent, // ওটিপি পাঠালে ফিল্ড লক হবে
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_email_24),
                                contentDescription = "Email",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = getDashboardTextFieldColors(),
                        trailingIcon = {
                            if (!isOtpSent) {
                                Button(
                                    onClick = {
                                        emailError = email.isBlank()
                                        if (!emailError) {
                                            otpViewModel.sendOtp(email)
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
                                    Text("Get OTP", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.outline_check_small_24),
                                    contentDescription = "Sent",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- ২. VERIFICATION CODE ROW ---
                    DashboardInputLabel(text = "Verification Code*")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = otpCode,
                            onValueChange = { otpViewModel.setOtp(it); otpError = false },
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
                            colors = getDashboardTextFieldColors(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Button(
                            onClick = {
                                emailError = email.isBlank()
                                otpError = otpCode.isBlank()
                                if (!emailError && !otpError) {
                                    otpViewModel.verifyOtp(email, otpCode)
                                }
                            },
                            enabled = !isOtpSuccess,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(125.dp).height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    isOtpSuccess -> Color(0xFF2E7D32)
                                    isOtpInvalid -> Color(0xFFD32F2F)
                                    else -> colorResource(R.color.primaryColor)
                                },
                                disabledContainerColor = Color(0xFF2E7D32)
                            )
                        ) {
                            Text(
                                text = when {
                                    isOtpSuccess -> "Verified ✓"
                                    isOtpInvalid -> "Retry ✕"
                                    else -> "Verify"
                                },
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- ৩. User Name ---
                    DashboardInputLabel(text = "User Name*")
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it; nameError = false },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter full name", color = Color.DarkGray) },
                        isError = nameError,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_person_24),
                                contentDescription = "User",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = getDashboardTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- ৪. Phone Number ---
                    DashboardInputLabel(text = "Phone Number*")
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; phoneError = false },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter phone number", color = Color.DarkGray) },
                        isError = phoneError,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_settings_phone_24),
                                contentDescription = "Phone",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = getDashboardTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- ৫. Password ---
                    DashboardInputLabel(text = "Password*")
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = false },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••", color = Color.DarkGray) },
                        isError = passwordError,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_password_24),
                                contentDescription = "Password",
                                tint = colorResource(R.color.primaryColor)
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = getDashboardTextFieldColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🚀 Submit Button
            Button(
                onClick = {
                    nameError = userName.isBlank()
                    emailError = email.isBlank()
                    phoneError = phone.isBlank()
                    passwordError = password.isBlank()

                    if (nameError || emailError || phoneError || passwordError) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // ওটিপি ভেরিফাইড না হলে অ্যাকাউন্ট তৈরি করতে দেবে না
                    if (!isOtpSuccess) {
                        Toast.makeText(context, "Please verify OTP first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val sessionManager = SessionManager(context)
                    val role = sessionManager.getRole()
                    val isAdmin = role == "admin"

                    if (!isAdmin) {
                        Toast.makeText(context, "Only admin can create account", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    viewModel.signup(userName, email, phone, password, sessionManager.getUserId())
                },
                enabled = !(loading || otpLoading),
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
                    text = "Create User",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // মডার্ন ফুলস্ক্রিন ব্লকিং লোডিং ওভারলে (Signup বা OTP লোডিং চললে চালু হবে)
        AnimatedVisibility(
            visible = loading || otpLoading,
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
fun DashboardInputLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = colorResource(R.color.textColor),
        modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
    )
}

// মডার্ন লাইট থিম টেক্সট ফিল্ড কালার স্কিম
@Composable
fun getDashboardTextFieldColors() = OutlinedTextFieldDefaults.colors(
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
fun PreviewUserScreen() {
    UserScreen(rememberNavController(), viewModel(), viewModel())
}