package com.example.massmanager.Login_File
import android.widget.ToggleButton
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.R

@Composable
fun LoginScreen(navController: NavController) {


    var selectedTab by remember { mutableStateOf("user") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier.fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Welcome back",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.weight(1f).padding(2.dp),
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
                modifier = Modifier.weight(1f).padding(2.dp),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 16.sp,
                textAlign = TextAlign.End,
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
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primaryColor)// Green color
                )
            ) {
                Text(
                    "Login ->",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
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
                        // Navigate Signup Screen
//                        navController.navigate("signup")
                    }
                ) {
                    Text(
                        text = "Sign up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF8A00)
                    )
                }
            }






        }















    }


}

@Preview(showSystemUi = true)
@Composable
fun loginui() {
    val navController= rememberNavController()

    LoginScreen(navController)
}