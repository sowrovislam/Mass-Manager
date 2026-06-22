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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.DataMail
import com.example.massmanager.Api_Otp.Data_Class.MealMonthGroup
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.ViewModel.GetMealViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealListScreen(navController: NavController, viewModel: GetMealViewModel) {

    val meals by viewModel.meals.collectAsState()
    val total by viewModel.totalMeal.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val userId = session.getUserId().toString()

    LaunchedEffect(Unit) {
        viewModel.loadMeals(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("চলমান সব মিল তালিক", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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

                // 🔹 Total Meal Counter Dashboard Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Total Meals Consumed",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "$total",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Loading and List Section
                if (loading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else if (meals.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No meal data available", color = Color.Gray, fontSize = 16.sp)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(meals) { monthGroup ->
                            MonthMealCard(monthGroup = monthGroup)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthMealCard(monthGroup: MealMonthGroup) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthGroup.month,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                SuggestionChip(
                    onClick = { },
                    label = { Text("Total: ${monthGroup.totalMeal}") },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            monthGroup.items.forEach { item ->
                MealItemRow(item = item)
            }
        }
    }
}

// 🔹 মডিফাইড Meal Item Row Component (দুপুর ও রাতের মিল কাউন্টার সহ)
@Composable
fun MealItemRow(item: DataMail) {
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
            // 👈 বামদিকের সেকশন: নাম, ইমেইল এবং ডেট
            Column(modifier = Modifier.weight(1.3f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF2C3E50)
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Date: ${item.date}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            // ☝️ মাঝখানের সেকশন: দুপুর ও রাতের কাউন্টার ব্যাজ (Vertical Alignment)
            Row(
                modifier = Modifier.weight(1.2f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MealStatusBadge(
                    label = "দুপুর",
                    count = item.isDupur
                )
                Spacer(modifier = Modifier.width(8.dp))
                MealStatusBadge(
                    label = "রাত",
                    count = item.isRat
                )
            }

            // 👉 ডানদিকের সেকশন: ওই দিনের সর্বমোট মিল সংখ্যা
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${item.counter} Meal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

// 🔹 দুপুর ও রাতের জন্য নতুন রি-ডিজাইনড স্ট্যাটাস এবং মিল কাউন্ট ব্যাজ
@Composable
fun MealStatusBadge(label: String, count: String) {
    // যদি মিল সংখ্যা "0" হয় তবে অফ (লাল), অন্যথায় অন (সবুজ)
    val isActive = count != "0" && count.isNotEmpty()
    val backgroundColor = if (isActive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (isActive) Color(0xFF2E7D32) else Color(0xFFC62828)

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
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        // ব্যাজের নিচে সুন্দর করে মিলের সংখ্যা দেখানোর টেক্সট
        Text(
            text = if (isActive) "$count টি" else "বন্ধ",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isActive) Color(0xFF555555) else Color.Gray
        )
    }
}