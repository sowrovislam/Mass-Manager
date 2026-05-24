package com.example.massmanager.Login_File

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.ViewModel.OtpViewModel
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(navController: NavController, viewModel: OtpViewModel) {


    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()


    val context = LocalContext.current
    val status by viewModel.status.collectAsState()
    val otp by viewModel.otpState.collectAsState()
    val status1 by viewModel.status1.collectAsState()
    var isClicked by remember { mutableStateOf(false) }
    var isOtpSent by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf(false) }

    val isSuccess = status == "OTP Verified Success"
    val isInvalid = status == "Invalid OTP"


    // admin login data
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Loding bar
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))



        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Create your account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp),
            textAlign = TextAlign.Start,
            color = colorResource(R.color.textColor)
        )

        Text(
            text = "Please provide your signup information to access the dashboard.",
            fontSize = 14.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 8.dp, start = 5.dp),
            color = colorResource(R.color.textColor)
        )
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            //User Name
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Email*", color = colorResource(R.color.textColor))


            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = email, onValueChange = {
                        email = it
                        emailError = false   // typing করলে error চলে যাবে
                    }, placeholder = {
                        Text("Enter Email")
                    }, modifier = Modifier
                        .weight(1f)
                        .height(60.dp),

                    isError = emailError,   // 🔥 red border

                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_email_24),
                            contentDescription = "email icon"
                        )
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),

                    singleLine = true,

                    trailingIcon = {
                        Button(
                            onClick = {

                                emailError = email.isBlank()

                                if (emailError) return@Button

                                viewModel.sendOtp(email)

                                isOtpSent = true   // 🔥 lock button

                            }, enabled = !isOtpSent,   // ❌ second click disable

                            shape = RoundedCornerShape(14.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOtpSent) colorResource(R.color.background) else Color.Red,
                                disabledContainerColor = Color.Gray
                            ),

                            contentPadding = PaddingValues(
                                horizontal = 16.dp, vertical = 6.dp
                            ), modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Text(
                                text = if (isOtpSent) "${status1}" else "Get OTP"
                            )
                        }
                    })
            }

            // code vertical

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Code*", color = colorResource(R.color.textColor))

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = otp, onValueChange = {
                        viewModel.setOtp(it)
                        otpError = false   // typing করলে error remove
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    placeholder = {
                        Text("Input Otp")
                    },

                    isError = otpError,   // 🔥 red border

                    modifier = Modifier.width(200.dp),

                    singleLine = true
                )

                Spacer(modifier = Modifier.width(16.dp))


                Button(
                    onClick = {

                        emailError = email.isBlank()
                        otpError = otp.isBlank()

                        if (emailError || otpError) return@Button

                        viewModel.verifyOtp(email, otp)

                    },

                    enabled = !isSuccess,   // 🔥 ONLY SUCCESS disables button

                    shape = RoundedCornerShape(14.dp),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            when {
                                isSuccess -> 55.dp
                                isInvalid -> 45.dp
                                else -> 50.dp
                            }
                        ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            isSuccess -> Color(0xFF2E7D32)
                            isInvalid -> Color(0xFFD32F2F)
                            else -> Color(0xFF1976D2)
                        }, disabledContainerColor = Color(0xFF2E7D32)
                    )
                ) {

                    Text(
                        text = when {
                            isSuccess -> "OTP Verified Success ✅"
                            isInvalid -> "Invalid OTP ❌"
                            else -> "Submit"
                        }, color = Color.White
                    )
                }


            }






            Spacer(modifier = Modifier.height(20.dp))



            Text(text = "Username*", color = colorResource(R.color.textColor))

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Monitor Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_person_24),
                        contentDescription = "email icon"
                    )
                })


            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Phone*", color = colorResource(R.color.textColor))

            OutlinedTextField(
                value = number, onValueChange = { number = it },

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("Enter Phone Number")
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),

                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.outline_settings_phone_24),
                        contentDescription = "email icon"
                    )
                }

            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Password*", color = colorResource(R.color.textColor))
            OutlinedTextField(
                value = password, onValueChange = { password = it },

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("Enter  Password")
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),

                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_password_24),
                        contentDescription = "email icon"
                    )
                }

            )






            Spacer(modifier = Modifier.heightIn(10.dp))

            Text(text = "${message}", color = Color.Red, modifier = Modifier.padding(8.dp))
//            Text(text = "${message}", color = Color.Red, modifier = Modifier.padding(8.dp))










            Button(
                onClick = {

                    // ✅ FIELD VALIDATION
                    val nameError = name.isBlank()
                    val emailError = email.isBlank()
                    val numberError = number.isBlank()
                    val passwordError = password.isBlank()

                    if (nameError || emailError || numberError || passwordError) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // ✅ OTP CHECK
                    when {
                        status.equals("OTP Verified Success", ignoreCase = true) -> {

                            Toast.makeText(context, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()

                            viewModel.register(name, email, number, password)

                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }

                        else -> {
                            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = !loading, // Disable the button while loading
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primaryColor)
                )
            ) {
                Text(
                    text = "Login ->",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }






        }
    }


}

@Preview(showSystemUi = true)
@Composable
fun signupui() {

    val navController = rememberNavController()

    val viewModel = OtpViewModel()

    SignupScreen(
        navController = navController, viewModel = viewModel
    )
}