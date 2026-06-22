package com.example.massmanager.Loading_Data_List

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Api_Otp.Data_Class.UserReport
import com.example.massmanager.ViewModel.ReportViewModel
import com.example.massmanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalMealListScreen(
    navController: NavController,
    viewModel: ReportViewModel = viewModel()
) {

    val list = viewModel.reportList.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.errorMessage.value
    val success = viewModel.successMessage.value

    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val userid = sessionManager.getUserId().toString()

    // 🔄 API Call
    LaunchedEffect(Unit) {
        viewModel.loadReport(userid)
    }

    // ✅ Success Toast
    LaunchedEffect(success) {
        success?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    // ✅ Scaffold (Top Bar Added)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("মিল ও বাজার স্টেটমেন্ট", fontWeight = FontWeight.Bold)
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
        ) {

            // 📄 List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                items(list) { item ->
                    InvoiceCard(item)
                }
            }

            // 🔄 Loading
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading...")
                    }
                }
            }

            // ❌ Error
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // 📭 Empty State
            if (!isLoading && list.isEmpty()) {
                Text(
                    text = "No Data Found",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun InvoiceCard(item: UserReport) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Text(item.month, fontSize = 13.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(item.email, fontSize = 13.sp)

            Divider(modifier = Modifier.padding(vertical = 6.dp))

            InfoRow("total_bajar", item.mess_total_bajar.toString())

            InfoRow("Total Meals  ", item.mess_total_meals.toString())

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .width(120.dp) // 👈 এখানে control করবা size
                        .padding(vertical = 6.dp),
                    thickness = 1.dp,
                    color = DividerDefaults.color
                )
            }
            InfoRow("Meal Rate", item.meal_rate)

             InfoRow("User Total Meals ", item.user_total_meals.toString())
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .width(120.dp) // 👈 এখানে control করবা size
                        .padding(vertical = 6.dp),
                    thickness = 1.dp,
                    color = DividerDefaults.color
                )
            }

            InfoRow("Meal Cost", item.user_meal_cost)

            Spacer(modifier = Modifier.height(6.dp))

            InfoRow("Bazar", item.user_total_bajar.toString())
            InfoRow("Payment", item.user_payment_amount)

            Divider(modifier = Modifier.padding(vertical = 6.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End // 👈 key line
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // 👈 key
                ) {

                    Text(
                        text = "Balance :",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = item.balance,
                        fontWeight = FontWeight.Bold,
                        color = if (item.balance.startsWith("-")) Color.Red else Color(0xFF2E7D32)
                    )
                }

                Text(
                    text = "Status: ${item.payment_status.uppercase()}",
                    fontSize = 13.sp,
                    color = if (item.payment_status == "paid") Color(0xFF2E7D32) else Color.Red
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp)

        Text(
            value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}