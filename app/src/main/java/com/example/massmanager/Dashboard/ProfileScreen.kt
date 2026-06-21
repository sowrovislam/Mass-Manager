package com.example.massmanager.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R
import com.example.massmanager.ViewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel() // ✅ Standard Way to inject ViewModel
) {
    val context = LocalContext.current

    // ১. একটি মাত্র SessionManager অবজেক্ট তৈরি এবং রিমেম্বার করা হলো
    val sessionManager = remember { SessionManager(context) }

    val isAdmin = remember { sessionManager.isAdmin() }
    val currentUserId = remember { sessionManager.getUserId().toString() }
    val currentUserName = remember { sessionManager.getUserName() ?: "No Name Found" }
    val currentUserEmail = remember { sessionManager.GetEmail() ?: "No Email Found" }

    // 🔥 VIEWMODEL OBSERVE DATA
    val serverUserList by userViewModel.users // ✅ Use 'by' for cleaner state access if type allows
    val loading by userViewModel.loading
    val message by userViewModel.message

    // 🔥 LOAD USERS ON FIRST OPEN (ADMIN ONLY)
    LaunchedEffect(Unit) {
        if (isAdmin) {
            userViewModel.loadUsers(currentUserId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isAdmin) "অ্যাডমিন কন্ট্রোল প্যানেল" else "ইউজার প্রোফাইল ",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.status_bar_green),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(start = 9.dp, end = 9.dp)
                .padding(horizontal = 16.dp) // সাইড প্যাডিং এক জায়গায় নিয়ে আসা হলো
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // ================= PROFILE CARD =================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.primaryColor)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAdmin) "Admin Control Panel" else "User Dashboard",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Name    :  $currentUserName", color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Email   :  $currentUserEmail", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // ================= ROLE BASED CONDITIONAL UI =================
            if (isAdmin) {
                Text(
                    text = "Registered Users List (Admin Only)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (loading) {
                    // লোডিং ইন্ডিকেটর
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(R.color.status_bar_green))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(serverUserList) { user ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(text = "Name: ${user.name}", style = MaterialTheme.typography.bodyLarge)
                                    Text(text = "Email: ${user.email}", color = Color.Gray)
                                    Text(text = "Phone: ${user.number}", fontWeight = FontWeight.Bold, color = Color.DarkGray)

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // এডমিন অ্যাকশন বাটন
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                userViewModel.deleteUser(user.email, currentUserId)
                                                // 💡 ডিলিট করার পর লিস্ট রিফ্রেশ করার জন্য আবার কল করতে পারেন:
                                                // userViewModel.loadUsers(currentUserId)
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Delete", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // সাধারণ USER UI
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Welcome back, $currentUserName!\nYou are currently logged in as a general user.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            // ================= LOGOUT PROCESS =================
            Button(
                onClick = {
                    sessionManager.logout() // সঠিক ভ্যারিয়েবল ব্যবহার করা হয়েছে
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout From Account", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    // প্রিভিউ ক্র্যাশ এড়াতে রিয়েল ViewModel-এর বদলে মক বা ডিফল্ট স্টেট পাস করা নিরাপদ।
    // তবে টেস্ট করার জন্য এভাবে রাখা যায় যদি ViewModel এ ডিপেন্ডেন্সি ইনজেকশন ক্র্যাশ না করে।
    ProfileScreen(navController)
}