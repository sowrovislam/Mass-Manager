package com.example.massmanager.Loading_Data_List

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.MealData
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.R
import com.example.massmanager.ViewModel.Meal_Ret_ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalMealRetScreen(
    navController: NavController,
    viewModel: Meal_Ret_ViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val userid = sessionManager.getUserId().toString()

    LaunchedEffect(Unit) {
        viewModel.loadMealData(userid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "মাসিক মিল রিপোর্ট",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.status_bar_green),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colorResource(R.color.status_bar_green)
                    )
                }

                viewModel.error.isNotEmpty() -> {
                    ErrorState(
                        errorMessage = viewModel.error,
                        onRetry = { viewModel.loadMealData(userid) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                viewModel.mealList.isEmpty() -> {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    MealListContent(mealList = viewModel.mealList)
                }
            }
        }
    }
}

@Composable
fun MealListContent(mealList: List<MealData>) {
    val totalBajar = mealList.sumOf { it.total_bajar }
    val totalMeals = mealList.sumOf { it.total_meals }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ১টি সাধারণ সামারি কার্ড
        item {
            SummaryCard(totalBajar = totalBajar, totalMeals = totalMeals)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ইতিহাসের রেকর্ডসমূহ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // অলস কলামে (LazyColumn) ডাটা প্রদর্শন
        items(mealList) { meal ->
            MealItemRow(meal = meal)
        }
    }
}

@Composable
fun SummaryCard(totalBajar: Double, totalMeals: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.status_bar_green).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "সর্বমোট সারাংশ",
                style = MaterialTheme.typography.labelLarge,
                color = colorResource(R.color.status_bar_green),
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "মোট বাজার খরচ", fontSize = 12.sp, color = Color.Gray)
                    Text(text = "৳ ${String.format("%,.2f", totalBajar)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "মোট মিল", fontSize = 12.sp, color = Color.Gray)
                    Text(text = "$totalMeals টি", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}@Composable
fun MealItemRow(meal: MealData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ১. মাসের নাম
            Text(
                text = meal.month,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ২. মোট বাজার খরচ
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "মোট বাজার খরচ:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "৳ ${String.format("%,.0f", meal.total_bajar)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.status_bar_green)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ৩. মোট মিল
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "মোট মিল:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "${meal.total_meals} টি",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ৪. ডিভাইডার (Divider)
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ৫. মোট মিল রেট
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "মিল রেট:", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "৳ ${meal.meal_rate}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Text(
        text = "এখনো কোনো মিলের তথ্য পাওয়া যায়নি।",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        modifier = modifier
    )
}

@Composable
fun ErrorState(errorMessage: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.status_bar_green))
        ) {
            Text("আবার চেষ্টা করুন", color = Color.White)
        }
    }
}