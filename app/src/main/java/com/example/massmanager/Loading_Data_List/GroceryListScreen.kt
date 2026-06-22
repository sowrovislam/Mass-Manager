package com.example.massmanager.Loading_Data_List

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.GroceryList
import com.example.massmanager.Api_Otp.Data_Class.MonthlyData
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.R
import com.example.massmanager.ViewModel.GroceryLisiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListShowScreen(navController: NavController) {

    val viewModel: GroceryLisiViewModel = viewModel()
    val monthlyDataList by viewModel.data.collectAsState()
    val grandTotalPrice by viewModel.total.collectAsState()

    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val userId = session.getUserId().toString()

    LaunchedEffect(Unit) {
        viewModel.loadData(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("বাজারের খরচ তালিকা", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FA))
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                // 🔹 সর্বমোট খরচের ড্যাশবোর্ড কার্ড (Grand Total Banner)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                                )
                            )
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Total Grocery Expenses",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "৳ ${String.format("%.2f", grandTotalPrice)}",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 লোডিং এবং মেইন লিস্ট সেকশন
                if (monthlyDataList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // ১ মাসের ডাটা নিয়ে ১টি কার্ড তৈরি হবে
                        items(monthlyDataList) { monthGroup ->
                            MonthGroceryCard(monthGroup = monthGroup)
                        }
                    }
                }
            }
        }
    }
}

// 🔹 প্রতি মাসের জন্য ১টি ইউনিক কার্ড (Month Grocery Card)
@Composable
fun MonthGroceryCard(monthGroup: MonthlyData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // মাসের নাম এবং ঐ মাসের টোটাল খরচ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthGroup.month,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1E3A8A)
                )

                SuggestionChip(
                    onClick = { },
                    label = { Text("Total: ৳${monthGroup.totalPrice}") },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.LightGray.copy(alpha = 0.4f)
            )

            // ঐ মাসের ভেতরের সকল বাজার আইটেমের লিস্ট রো (Row)
            monthGroup.items.forEach { item ->
                GroceryItemRow(item = item)
            }
        }
    }
}

// 🔹 ১টি সিঙ্গেল বাজার আইটেমের রো ডিজাইন (Grocery Item Row Component)
@Composable
fun GroceryItemRow(item: GroceryList) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBFCFD)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 👈 বামদিকের সেকশন: প্রোডাক্টের নাম এবং তারিখ
            Column(modifier = Modifier.weight(1.3f)) {
                Text(
                    text = item.productname,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF2C3E50),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "তারিখ: ${item.date}",
                    fontSize = 11.sp,
                    color = Color(0xFF3B82F6),
                    fontWeight = FontWeight.Medium
                )
            }

            // ☝️ মাঝখানের সেকশন: ওজন ও দরের ব্যাজ (Vertical Alignment)
            Row(
                modifier = Modifier.weight(1.4f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GroceryStatusBadge(label = "ওজন", value = item.weight, isWeight = true)
                Spacer(modifier = Modifier.width(8.dp))
                GroceryStatusBadge(label = "দাম", value = "৳${item.price}", isWeight = false)
            }

            // 👉 ডানদিকের সেকশন: ওই প্রোডাক্টের মোট দাম এবং ক্রেতার নাম
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.weight(1.1f)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFE8F5E9), // হালকা সবুজ ব্যাকগ্রাউন্ড
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "৳${item.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF2E7D32)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ক্রেতা: ${item.name}",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// 🔹 ওজন এবং দরের জন্য সুন্দর কাউন্টার ব্যাজ
@Composable
fun GroceryStatusBadge(label: String, value: String, isWeight: Boolean) {
    val backgroundColor = if (isWeight) Color(0xFFE3F2FD) else Color(0xFFFFF3E0)
    val textColor = if (isWeight) Color(0xFF1E88E5) else Color(0xFFFB8C00)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF555555),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}