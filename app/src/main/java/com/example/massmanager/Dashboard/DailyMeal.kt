package com.example.massmanager.Dashboard

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.R
import com.example.massmanager.ViewModel.MealViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.colorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyMeal(navController: NavController, viewModel: MealViewModel) {
    var selectedDate by remember { mutableStateOf(Date()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var isDupurCounter by remember { mutableStateOf(1) }
    var isRatCounter by remember { mutableStateOf(1) }
    val TotalMealCount = isDupurCounter + isRatCounter
    var selected by remember { mutableStateOf<String?>(null) }

    // ✅ বোথ লোডিং স্টেট (Add and Delete)
    val loading = viewModel.loading.value
    val LoadingDalate = viewModel.loadingDelate.collectAsState().value

    var isSelected by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    val formattedDate = SimpleDateFormat(
        "dd MMMM, yyyy",
        Locale.getDefault()
    ).format(selectedDate)

    val currentTime = remember {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Meal", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.status_bar_green), // SAME as status bar
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        // ✅ ROOT BOX (Applied innerPadding here safely to fix AppBar layout rendering)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Reduced space slightly since TopAppBar already handles top positioning padding cleanly

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFF5252).copy(alpha = 0.08f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "⚠️ নোটিশ: ",
                                color = Color(0xFFEF2121),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "সকাল ৯:০০ AM এর পর কোনো মিল (Add বা Delete) করা যাবে না।",
                                modifier = Modifier.basicMarquee(),
                                color = Color(0xFFEF2121),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "MONTHLY INSIGHT",
                    fontSize = 14.sp,
                    color = Color(0xFF8A4F00)
                )

                Text(
                    text = "Current Time: $currentTime",
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Selected Date",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                // ✅ CLICK DATE
                Text(
                    text = formattedDate,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        showDatePicker = true
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 🔵 DUPUR COUNTER
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "দুপুর",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (isDupurCounter > 0) isDupurCounter--
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color.White, CircleShape)
                                    ) {
                                        Icon(
                                            painterResource(R.drawable.outline_check_indeterminate_small_24),
                                            contentDescription = "Decrease",
                                            tint = Color.Red
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = isDupurCounter.toString(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    IconButton(
                                        onClick = { isDupurCounter++ },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color.White, CircleShape)
                                    ) {
                                        Icon(
                                            painterResource(R.drawable.outline_add_24),
                                            contentDescription = "Increase",
                                            tint = Color(0xFF4CAF50)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))

                            // 🔻 VERTICAL DIVIDER
                            HorizontalDivider(
                                modifier = Modifier
                                    .height(80.dp)
                                    .width(1.dp),
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(12.dp))

                            // 🔴 RAT COUNTER
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "রাত",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (isRatCounter > 0) isRatCounter--
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color.White, CircleShape)
                                    ) {
                                        Icon(
                                            painterResource(R.drawable.outline_check_indeterminate_small_24),
                                            contentDescription = "Decrease",
                                            tint = Color.Red
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = isRatCounter.toString(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    IconButton(
                                        onClick = { isRatCounter++ },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(Color.White, CircleShape)
                                    ) {
                                        Icon(
                                            painterResource(R.drawable.outline_add_24),
                                            contentDescription = "Increase",
                                            tint = Color(0xFF4CAF50)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // ================= YES CARD (Add Meal) =================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val userId = sessionManager.getUserId()
                            val name = sessionManager.getUserName()
                            val email = sessionManager.GetEmail()

                            if (isDupurCounter == 0 && isRatCounter == 0) {
                                Toast.makeText(context, "Meal count cannot be 0", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }

                            viewModel.addMeal(
                                userid = userId.toString(),
                                name = name ?: "Daily Meal",
                                date = formattedDate,
                                counter = TotalMealCount.toString(),
                                isDupur = isDupurCounter.toString(),
                                isRat = isRatCounter.toString(),
                                email = email,
                                onResult = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF23D331) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "খাব (YES)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = "I consumed milk/puja today",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✓",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ================= NO CARD (Delete Meal) =================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val email = sessionManager.GetEmail()
                            viewModel.deleteMeal(
                                email = email.toString(),
                                date = formattedDate,
                                onResult = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected == "NO") Color(0xFFFFEBEE) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "খাব না (Delete)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                            Text(
                                text = "I skipped today",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (selected == "NO") Color(0xFFC62828) else Color.LightGray,
                                    shape = RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selected == "NO") "✓" else "×",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            // ✅ ২য় প্রোগ্রেস বার কন্ডিশন (Add অথবা Delete যেকোনো একটা কাজ করলেই স্ক্রিন লক হবে)
            if (loading || LoadingDalate) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable(enabled = false) { }, // ব্যাকগ্রাউন্ড ক্লিক টোটাল বন্ধ রাখবে
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
            }
        }
    }

    // ================= DATE PICKER =================
    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val cal = Calendar.getInstance()
                cal.set(year, month, day)
                selectedDate = cal.time
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun login() {
    val navController = rememberNavController()
    val viewModel = MealViewModel()
    DailyMeal(navController, viewModel)
}