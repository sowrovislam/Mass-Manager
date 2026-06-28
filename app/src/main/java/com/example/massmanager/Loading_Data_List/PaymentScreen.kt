package com.example.massmanager.Loading_Data_List

// 🔥 IMPORTS
import android.widget.DatePicker
import android.widget.Toast // টোস্ট ইমপোর্ট করা হয়েছে
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.ViewModel.MainViewModel
import com.example.massmanager.R
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay

// ================= HELPER =================

fun isCurrentOrFutureMonth(monthYear: String): Boolean {
    return try {
        val format = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val selectedDate = format.parse(monthYear) ?: return false

        val now = Calendar.getInstance()
        val selectedCal = Calendar.getInstance().apply { time = selectedDate }

        if (selectedCal.get(Calendar.YEAR) > now.get(Calendar.YEAR)) {
            true
        } else if (selectedCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            selectedCal.get(Calendar.MONTH) >= now.get(Calendar.MONTH)
        } else {
            false
        }
    } catch (e: Exception) {
        false
    }
}

// ================= SCREEN =================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    val sessionManager = remember { SessionManager(context) }
    val isAdmin = remember { sessionManager.isAdmin() }
    val currentUserId = sessionManager.getUserId().toString()

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    var email by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedMonthYear by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    var showDatePicker by remember { mutableStateOf(false) }

    // 🔥 এরর মেসেজ ট্র্যাকিং স্টেট
    var amountValidationError by remember { mutableStateOf("") }

    val dueData = viewModel.dueData
    val message = viewModel.message
    val loading = viewModel.loading

    val dueAmount = dueData?.due_amount?.toDoubleOrNull() ?: 0.0

    // 🔥 API CALL (শুধুমাত্র ডেটা ফেচিংয়ের জন্য)
    LaunchedEffect(email, selectedMonthYear) {
        if (!isAdmin) return@LaunchedEffect

        if (email.isEmpty() || selectedMonthYear.isEmpty()) {
            viewModel.dueData = null
            return@LaunchedEffect
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            viewModel.dueData = null
            return@LaunchedEffect
        }

        delay(500)
        // নতুন ডেটা ফেচ হলে পুরানো লোকাল এরর রিমুভ করে দেওয়া হচ্ছে
        amountValidationError = ""
        viewModel.fetchDue(currentUserId, email, selectedMonthYear)
    }

    // 🔥 সার্ভার থেকে সাকসেস বা এরর মেসেজ আসলে সেটি টোস্ট আকারে দেখাবে
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            // পেমেন্ট সফল হলে ফিল্ড রিফ্রেশ করার লজিক (ঐচ্ছিক)
            if (message.contains("success", ignoreCase = true) || message.contains("সফল", ignoreCase = true)) {
                amount = "" // পেমেন্ট হয়ে গেলে ফিল্ড ফাঁকা করে দেবে
                viewModel.fetchDue(currentUserId, email, selectedMonthYear) // আপডেট ডাটা দেখার জন্য রিলোড
            }
        }
    }

    // AUTO FILL
    LaunchedEffect(dueAmount) {
        if (dueAmount > 0) {
            amount = dueAmount.toString()
            amountValidationError = "" // অটোফিল হলে এরর ক্লিয়ার হবে
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Payment Panel", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.status_bar_green),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        if (!isAdmin) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🔒 অ্যাক্সেস সীমাবদ্ধ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "এই পেমেন্ট প্যানেলটি শুধুমাত্র ম্যানেজার বা অ্যাডমিনদের ব্যবহারের জন্য সংরক্ষিত।",
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("ফিরে যান", color = Color.White)
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(10.dp))

                // HEADER
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Payment Panel", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Easy monthly payment system", fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // DATE FIELD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                ) {
                    OutlinedTextField(
                        value = selectedMonthYear,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = { Text("Select Month", color = Color.Black) },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Black,
                            disabledLabelColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // EMAIL
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("User Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                if (loading) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }

                // CURRENT OR FUTURE MONTH NOTICE
                if (isCurrentOrFutureMonth(selectedMonthYear)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_arrow_drop_down_24),
                                contentDescription = "Warning",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = "সতর্কবার্তা",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                letterSpacing = 0.5.sp
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "এই মাসের তথ্য এখনো উপলব্ধ নয়",
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "মাস শেষ হলে আপনি তথ্য দেখতে এবং পেমেন্ট করতে পারবেন।",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                } else {
                    dueData?.let { due ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    "User Info",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Name: ${due.name}")
                                Text("Email: ${due.email}")
                                Text("Month: ${due.month}")
                                Spacer(Modifier.height(10.dp))
                                HorizontalDivider()
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "Due: ৳${due.due_amount}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = if (dueAmount > 0)
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        if (dueAmount <= 0) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "No Due",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    if (dueAmount > 0) {
                        // 🔥 AMOUNT FIELD (with validation UI update)
                        OutlinedTextField(
                            value = amount,
                            onValueChange = {
                                amount = it
                                amountValidationError = "" // ইউজার টাইপ করা শুরু করলে এরর চলে যাবে
                            },
                            label = { Text("Amount") },
                            singleLine = true,
                            isError = amountValidationError.isNotEmpty(), // এরর থাকলে বর্ডার লাল হবে
                            modifier = Modifier.fillMaxWidth()
                        )

                        // ডাইনামিক এরর মেসেজ ডিসপ্লে
                        if (amountValidationError.isNotEmpty()) {
                            Text(
                                text = amountValidationError,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val enteredAmount = amount.toDoubleOrNull() ?: 0.0

                                // 🔥 বকেয়া এবং ইনপুট দেওয়া অ্যামাউন্ট মেলানোর কন্ডিশন
                                if (enteredAmount != dueAmount) {
                                    amountValidationError = "⚠️ বকেয়া পরিমাণ (৳$dueAmount) এবং পেমেন্টের পরিমাণ অবশ্যই সমান হতে হবে।"
                                } else {
                                    amountValidationError = "" // ভ্যালিডেশন সাকসেসফুল
                                    // 🚀 সার্ভারে সেন্ড হচ্ছে, সাকসেস হলে মেসেজ টোস্ট আকারে আসবে
                                    viewModel.insertPayment(currentUserId, email, amount, selectedMonthYear)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "Pay Now",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }

        if (showDatePicker && isAdmin) {
            BuiltInMonthYearSpinnerPicker(
                onDismiss = { showDatePicker = false },
                onDateSelected = { formattedDate ->
                    selectedMonthYear = formattedDate
                    showDatePicker = false
                }
            )
        }
    }
}

// ================= MONTH YEAR SPINNER PICKER =================

@Composable
fun BuiltInMonthYearSpinnerPicker(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val currentCalendar = Calendar.getInstance()
    var chosenMonth by remember { mutableStateOf(currentCalendar.get(Calendar.MONTH)) }
    var chosenYear by remember { mutableStateOf(currentCalendar.get(Calendar.YEAR)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Month & Year", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { ctx ->
                        DatePicker(ctx, null, android.R.style.Theme_Holo_Light_Dialog_NoActionBar).apply {
                            calendarViewShown = false
                            spinnersShown = true

                            val daySpinnerId = ctx.resources.getIdentifier("day", "id", "android")
                            if (daySpinnerId != 0) {
                                val daySpinner = findViewById<android.view.View>(daySpinnerId)
                                daySpinner?.visibility = android.view.View.GONE
                            }

                            init(chosenYear, chosenMonth, 1) { _, year, monthOfYear, _ ->
                                chosenYear = year
                                chosenMonth = monthOfYear
                            }
                        }
                    },
                    modifier = Modifier.wrapContentSize()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val resultCal = Calendar.getInstance().apply {
                        set(Calendar.YEAR, chosenYear)
                        set(Calendar.MONTH, chosenMonth)
                    }
                    val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    onDateSelected(sdf.format(resultCal.time))
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}