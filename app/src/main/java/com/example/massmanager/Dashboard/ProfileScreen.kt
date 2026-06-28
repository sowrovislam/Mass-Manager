package com.example.massmanager.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R
import android.net.Uri
import com.example.massmanager.ViewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel() // ✅ Standard Way to inject ViewModel
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
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
                modifier = Modifier
                    .fillMaxWidth()
                   , // হালকা মার্জিন দিয়ে ফ্রেম সুন্দর করা হয়েছে
                shape = RoundedCornerShape(24.dp), // মডার্ন কার্ভড কর্নার
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.primaryColor)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // হালকা ইলিভেশন
            ) {
                Column(modifier = Modifier.padding(24.dp)) { // প্যাডিং একটু বাড়িয়ে ১২ থেকে ২৪ করা হয়েছে

                    // ================= HEADER SECTION =================
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isAdmin) "Admin Control Panel" else "User Dashboard",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "Welcome back!",
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // একটি স্টাইলিশ রোল ব্যাজ (Admin/User)
                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = if (isAdmin) "👑 Admin" else "👤 Member",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // ================= INFO BLOCK =================
                    // নাম এবং ইমেইলের "Name : " এই ম্যানুয়াল স্পেসিং বাদ দিয়ে Row এবং আইকন দিয়ে সাজানো হয়েছে

                    // ১. নাম row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_person_24), // আপনার প্রজেক্টের আইকন দিন
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "NAME",
                                color = Color.White.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = currentUserName,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ২. ইমেইল row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_email_24), // আপনার প্রজেক্টের আইকন দিন
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "EMAIL ADDRESS",
                                color = Color.White.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = currentUserEmail,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                            )
                        }
                    }
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

                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(
                                                0xFF00BCD4
                                            )
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Update", color = Color.White)
                                        }
// ২. আপনার ডিলিট বাটন
                                        Button(
                                            onClick = {
                                                showDeleteDialog = true // বাটনে ক্লিক করলে ডায়ালগ চালু হবে
                                            },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Delete", color = Color.White)
                                        }

// ৩. 🔴 কনফার্মেশন অ্যালার্ট ডায়ালগ লজিক
                                        if (showDeleteDialog) {
                                            AlertDialog(
                                                onDismissRequest = {
                                                    showDeleteDialog =
                                                        false // ডায়ালগের বাইরে ক্লিক করলে বন্ধ হবে
                                                },
                                                title = {
                                                    Text(
                                                        text = "ইউজার ডিলিট করুন",
                                                        fontWeight = FontWeight.Bold,
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
                                                },
                                                text = {
                                                    Text(
                                                        text = "আপনি কি নিশ্চিতভাবে এই ইউজারটি ডিলিট করতে চান? এই কাজটি আর ফিরিয়ে আনা যাবে না।",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                },
                                                confirmButton = {
                                                    Button(
                                                        onClick = {
                                                            showDeleteDialog =
                                                                false // ডায়ালগ বন্ধ করুন

                                                            // 🎯 আসল ডিলিট অ্যাকশন এখানে কাজ করবে
                                                            userViewModel.deleteUser(
                                                                user.email,
                                                                currentUserId
                                                            )
                                                        },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = Color(0xFFE53935)
                                                        ) // ডিলিট বাটন লাল কালার
                                                    ) {
                                                        Text(
                                                            "হ্যাঁ, ডিলিট করুন",
                                                            color = Color.White
                                                        )
                                                    }
                                                },
                                                dismissButton = {
                                                    TextButton(
                                                        onClick = {
                                                            showDeleteDialog =
                                                                false // ক্যানসেল করলে ডায়ালগ বন্ধ হবে
                                                        }
                                                    ) {
                                                        Text("না, ক্যানসেল", color = Color.Gray)
                                                    }
                                                },
                                                shape = RoundedCornerShape(16.dp) // ডায়ালগের কর্নার সুন্দর করার জন্য
                                            )


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