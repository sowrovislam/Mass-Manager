package com.example.massmanager.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.GroceryItem
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.R
import com.example.massmanager.ViewModel.GroceryViewModel
import com.example.massmanager.ViewModel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListScreen(
    navController: NavController,
    viewModel: GroceryViewModel
) {
    var showSaveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val ScheduleViewModel: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    var name by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    // Weight Unit Dropdown State
    var expanded by remember { mutableStateOf(false) }
    val unitOptions = listOf("gm", "kg", "pcs", "litre")
    var selectedUnit by remember { mutableStateOf(unitOptions[0]) }

    val groceryList = remember { mutableStateListOf<GroceryItem>() }

    val userEmail = session.GetEmail() ?: ""
    val userName = session.getUserName() ?: "Test User"
    val userId = session.getUserId().toString()
    val today by ScheduleViewModel.todayItem.collectAsState()

    LaunchedEffect(Unit) {
        if (userId.isNotEmpty() && userId != "null") {
            ScheduleViewModel.loadSchedule(userId.toInt())
        }
    }

    // 🛡️ 1. STRONG EMAIL CHECK CONDITION
    val EmailSame = remember(userEmail, today?.email) {
        val cleanUserEmail = userEmail.trim().lowercase()
        val cleanTodayEmail = today?.email?.toString()?.trim()?.lowercase() ?: ""

        // নিশ্চিত করা হচ্ছে যে ইমেইল দুটি খালি নয় এবং একে অপরের সাথে হুবহু মিলছে
        cleanUserEmail.isNotEmpty() && cleanTodayEmail.isNotEmpty() && cleanUserEmail == cleanTodayEmail
    }

    // 🛡️ 2. STRONG NOTICE CONDITION
    val showNotScheduledNotice = remember(today, userEmail, EmailSame) {
        val isDataLoaded = today != null // নিশ্চিত করা যে সার্ভার থেকে আজকের ডাটা লোড সম্পন্ন হয়েছে
        val isEmailEmpty = userEmail.trim().isEmpty() || today?.email?.toString()?.trim().isNullOrEmpty()

        // ডাটা লোড হয়েছে এবং ইমেইল ফাঁকা নেই, কিন্তু ইমেইল ম্যাচ করেনি (ভিন্ন ইউজার)
        isDataLoaded && !isEmailEmpty && !EmailSame
    }

    val totalAmount = groceryList.sumOf {
        it.price.toDoubleOrNull() ?: 0.0
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("চলমান বাজার তালিকা", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
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
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // 🛑 শিডিউল না থাকলে অথবা ভিন্ন ইউজার হলে নোটিশ কার্ড দেখাবে
            if (showNotScheduledNotice) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "⚠️ নোটিশ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "আজকে আপনার বাজার করার শিডিউল নেই!",
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color(0xFFC62828),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 🔹 INPUT CARD (EmailSame ট্রু হলেই শুধু একটিভ থাকবে)
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Add New Item",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (EmailSame) MaterialTheme.colorScheme.primary else Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Item Name (e.g., Rice)") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = EmailSame,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = weightInput,
                            onValueChange = { input ->
                                weightInput = if (input.isEmpty()) "0" else input
                            },
                            label = { Text("Weight/Qty") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1.2f),
                            enabled = EmailSame,
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenuBox(
                            expanded = expanded && EmailSame,
                            onExpandedChange = { if (EmailSame) expanded = !expanded },
                            modifier = Modifier.weight(0.8f)
                        ) {
                            OutlinedTextField(
                                value = selectedUnit,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Unit") },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_arrow_drop_down_24),
                                        contentDescription = if (expanded) "Close Menu" else "Open Menu"
                                    )
                                },
                                modifier = Modifier.menuAnchor(),
                                enabled = EmailSame,
                                shape = RoundedCornerShape(10.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = expanded && EmailSame,
                                onDismissRequest = { expanded = false }
                            ) {
                                unitOptions.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit) },
                                        onClick = {
                                            selectedUnit = unit
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price (৳)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = EmailSame,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank() && weightInput.isNotBlank() && price.isNotBlank()) {
                                val finalWeight = "${weightInput.trim()} $selectedUnit"
                                groceryList.add(
                                    GroceryItem(
                                        name = name.trim(),
                                        weight = finalWeight,
                                        price = price.trim()
                                    )
                                )
                                name = ""
                                weightInput = ""
                                price = ""
                            }
                        },
                        enabled = EmailSame,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("➕ Add to List", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 💰 TOTAL AMOUNT BOX
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Amount:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "৳ $totalAmount",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 📦 GROCERY ITEMS LIST
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(groceryList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "📦 ${item.name}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "⚖ Weight: ${item.weight}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "৳ ${item.price}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { groceryList.remove(item) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_delete_24),
                                        contentDescription = "Delete",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 💾 SERVER SAVE SECTION
            Button(
                onClick = {
                    showSaveDialog = true // বাটনে ক্লিক করলে ডায়ালগটি ওপেন হবে
                },
                enabled = groceryList.isNotEmpty() && !viewModel.loading.value && EmailSame,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                if (viewModel.loading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("💾 Save All to Server", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }

// ৩. 💾 প্রিমিয়াম কনফার্মেশন অ্যালার্ট ডায়ালগ ইউআই
            if (showSaveDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showSaveDialog = false // ডায়ালগের বাইরে ক্লিক করলে বন্ধ হবে
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "💾 ", fontSize = 22.sp)
                            Text(
                                text = "ডাটা সেভ নিশ্চিত করুন",
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF2E7D32) // সুন্দর গ্রিন থিম টাইটেল
                            )
                        }
                    },
                    text = {
                        Text(
                            text = "আপনি কি এই তালিকার সব আইটেম সার্ভারে সেভ করতে চান? নিশ্চিত করতে নিচে ক্লিক করুন।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSaveDialog = false // প্রথমে ডায়ালগটি বন্ধ হবে

                                // 🎯 সার্ভারে ডাটা সেভ করার আসল ফাংশন
                                viewModel.saveAllItems(
                                    list = groceryList.toList(),
                                    name = userName,
                                    email = userEmail,
                                    userid = userId
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // গ্রিন কনফার্ম বাটন
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("হ্যাঁ, সেভ করুন", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showSaveDialog = false // ক্যানসেল করলে ডায়ালগ বন্ধ হবে
                            }
                        ) {
                            Text(
                                text = "না, ক্যানসেল",
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    shape = RoundedCornerShape(20.dp), // মডার্ন কার্ভড ডায়ালগ কর্নার
                    containerColor = Color.White // ব্যাকগ্রাউন্ড কালার হোয়াইট
                )
            }

            // 📩 SERVER MESSAGE
            if (viewModel.message.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (viewModel.message.value.contains("Successfully")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.message.value,
                        fontWeight = FontWeight.Bold,
                        color = if (viewModel.message.value.contains("Successfully")) Color(0xFF2E7D32) else Color(0xFFC62828),
                        modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}