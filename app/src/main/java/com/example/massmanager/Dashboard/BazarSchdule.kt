package com.example.massmanager.Dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.ViewModel.ScheduleViewModel

@Composable
fun BazarShdule(navController: NavController,viewModel: ScheduleViewModel){

    val context = LocalContext.current
    val res by viewModel.response.collectAsState()
    val today by viewModel.todayItem.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val sessionManager = SessionManager(context)

    val id= sessionManager.getUserId()



    LaunchedEffect(Unit) {
        viewModel.load(id) // শুধু id দিলেই হবে
//         viewModel.load(id, "2026-06-10") // optional date
    }

    when {

        loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        error.isNotEmpty() -> {
            Text("Error: $error")
        }

        else -> {


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues( top = 50.dp)
    ) {

        // 🔥 TODAY CARD (direct UI inside LazyColumn)
        item {

            today?.let { data ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 8.dp,
                            end = 8.dp,
                            top = 6.dp,
                            bottom = 10.dp
                        ), // 🔥 outside spacing (screen থেকে দূরে)

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50)
                    ),

                    elevation = CardDefaults.cardElevation(6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 12.dp,
                            top = 12.dp,
                            bottom = 12.dp
                        ) // 🔥 inner spacing (text ভিতরে সুন্দর distance)
                    ) {

                        Text(
                            text = "🔥 TODAY ACTIVE",
                            color = Color.White
                        )

                        Text(
                            text = data.name,
                            color = Color.White
                        )

                        Text(
                            text = data.email,
                            color = Color.White
                        )

                        Text(
                            text = data.number,
                            color = Color.White
                        )

                        Text(
                            text = "Date: ${data.start_date} → ${data.end_date}",
                            color = Color.White
                        )
                    }
                }}
        }





        // 📋 FULL LIST
        items(res?.schedule ?: emptyList()) { item ->

            val isActive = res?.today?.let { it in item.start_date..item.end_date }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 6.dp,
                        bottom = 10.dp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isActive == true) Color(0xFFE8F5E9) else Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Column(modifier = Modifier.padding(12.dp)) {

                    Text(text = item.name)
                    Text(text = "${item.start_date} → ${item.end_date}")

                    if (isActive == true) {
                        Text("🔥 ACTIVE TODAY", color = Color.Green)
                    }
                }
            }
        }}




        }
    }







}