package com.example.massmanager.Login_File

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.LoginViewModel
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Api_Otp.Data_Class.SignUpViewModel
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R

@Composable
fun LoginScreen(navController: NavController,viewModel: LoginViewModel) {

//    val viewMode: SignUpViewModel = viewModel()


    val context= LocalContext.current
    var selectedTab by remember { mutableStateOf("user") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()
    val success by viewModel.success.collectAsState()
    val user by viewModel.user.collectAsState()

    Log.d("USER_NAME", user?.id.toString())

    LaunchedEffect(success) {
        if (success) {
            navController.navigate(Screen.dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
    val loginData = viewModel.loginState.value

    val success1 = loginData != null && loginData.status == "success"

    LaunchedEffect(success1) {
        if (success1) {
            navController.navigate(Screen.dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Welcome back",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp),
            textAlign = TextAlign.Start,
            color = colorResource(R.color.textColor)
        )

        Text(
            text = "Please enter your credentials to access your monitor dashboard.",
            fontSize = 14.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(top = 8.dp, start = 5.dp),
            color = colorResource(R.color.textColor)
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Toggle Button (User / Admin)


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))

        ) {

            // User Button
            Button(
                onClick = { selectedTab = "user" },
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "user") Color(0xFF0D5C63) else Color.Transparent,
                    contentColor = if (selectedTab == "user") Color.White else Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                elevation = null
            ) {
                Text("User Login")
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Admin Button
            Button(
                onClick = { selectedTab = "admin" },
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "admin") Color(0xFF0D5C63) else Color.Transparent,
                    contentColor = if (selectedTab == "admin") Color.White else Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                elevation = null
            ) {
                Text("Admin Login")
            }
        }

        // Start Login  Code
        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(text = "Email or Username*", color = colorResource(R.color.textColor))

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_email_24),
                        contentDescription = "email icon"
                    )
                }
            )




            Spacer(modifier = Modifier.height(25.dp))

            Text("Password*", color = colorResource(R.color.textColor))

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_password_24),
                        contentDescription = "email icon"
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 16.sp,
                textAlign = TextAlign.End,

                color = colorResource(id = R.color.textColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                        navController.navigate(Screen.Forgate.route)


                    }

            )

            // hare show err  smss
            Text(
                text = "",
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.textColor)
            )





            Spacer(modifier = Modifier.height(18.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )

                Text(
                    text = "I Agree The Terms and Conditions?",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {

                    val isEmailEmpty = email.isBlank()
                    val isPasswordEmpty = password.isBlank()
                    val isTermsNotAccepted = !isChecked

                    if (isEmailEmpty || isPasswordEmpty) {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (isTermsNotAccepted) {
                        Toast.makeText(context, "Accept Terms & Conditions", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    when (selectedTab) {

                        "admin" -> {

//                            val UserId= user?.id
//                            val UserName= user?.name.toString()
//                            val sessionManager = SessionManager(context)
//                            sessionManager.saveLogin(UserId, UserName)

                            viewModel.admin_login(email, password,context)
                            Toast.makeText(context, "${message}", Toast.LENGTH_SHORT).show()
                        }

                        "user" -> {

                            viewModel.user_login(email,password,context)


                            Toast.makeText(context, "User Login", Toast.LENGTH_SHORT).show()

                        }else -> {

                            loading==false

                        Toast.makeText(context, "Not Login", Toast.LENGTH_SHORT).show()
                        }
                    }

                },
                enabled = !loading, // Disable the button while loading
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primaryColor)
                )
            ) {
                Text(
                    text = if (loading) "Loading..." else "Login ->",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )

            Text(
                text = "Or continue with",
                modifier = Modifier.padding(horizontal = 12.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(28.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "New to the market? ",
                fontSize = 14.sp,
                color = Color.Gray
            )

            TextButton(
                onClick = {

                    navController.navigate(Screen.Signup.route)
                }
            ) {
                Text(
                    text = "Sign up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF8A00),

                    )
            }
        }







        if (loading) {

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }


























    }


}

@Preview(showSystemUi = true)
@Composable
fun loginui() {
    val navController = rememberNavController()
     val viewModel= LoginViewModel()
    LoginScreen(navController,viewModel)
}