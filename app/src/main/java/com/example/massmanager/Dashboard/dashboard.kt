package com.example.massmanager.Dashboard

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.ViewModel.LoginViewModel
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Login_File.LoginScreen
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R
import com.example.massmanager.ViewModel.ScheduleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dashboardScreen(navController: NavController, viewModel: ScheduleViewModel= viewModel()) {
    val today by viewModel.todayItem.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val meals by viewModel.meals.collectAsState()


    val context = LocalContext.current
    val session = SessionManager(context)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val loading by viewModel.loading.collectAsState()
    val sessionManager = SessionManager(context)
    sessionManager.getUserId()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    LaunchedEffect(Unit) {

        viewModel.loadSchedule(sessionManager.getUserId()) // শুধু id দিলেই হবে
//         viewModel.load(id, "2026-06-10") // optional date
        viewModel.loadMeals(sessionManager.getUserId().toString())

    }

            val view = LocalView.current

    val statusBarColor = colorResource(R.color.status_bar_green)// same as your theme green

    SideEffect {
        val window = (view.context as Activity).window

        window.statusBarColor = statusBarColor.toArgb()

        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = false
    }

    val items = listOf("হোম", "দৈনিক খাবার", "ইউজার")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                // ✅ fillMaxWidth() এর বদলে স্ট্যান্ডার্ড উইডথ এবং প্যাডিং ব্যবহার করা হয়েছে
                modifier = Modifier
                    .width(320.dp)
                    .fillMaxHeight(),
                drawerContainerColor = colorResource(R.color.teal_700),
                drawerShape = androidx.compose.foundation.shape.RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {

                // ================= DRAWER HEADER =================
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "মেস ম্যানেজার",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color.White
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.White.copy(alpha = 0.2f) // হালকা ও মার্জিত ডিভাইডার
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ================= DRAWER ITEMS =================

                // ১. ড্যাশবোর্ড ম্যানেজার
                NavigationDrawerItem(
                    label = { Text("ড্যাশবোর্ড ম্যানেজার") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // ২. প্রোফাইল
                NavigationDrawerItem(
                    label = { Text("প্রোফাইল") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Profile.route)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.White, // ✅ টেক্সট ও আইকন কালার এক রাখা হয়েছে স্ট্যান্ডার্ডের জন্য
                        unselectedIconColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // ৩. বাজার তালিকা
                NavigationDrawerItem(
                    label = { Text("বাজার কারীর সময়সূচি") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Shdule.route)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )



                // 4. বাজার তালিকা
                NavigationDrawerItem(
                    label = { Text("চলমান সব মিল তালিকা") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MealListScreen.route)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
          // 5. বাজার তালিকা
                NavigationDrawerItem(
                    label = { Text("বাজারের খরচ তালিকা") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.GroceryListShow.route)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // 6. বাজার তালিকা
                NavigationDrawerItem(
                    label = { Text("মাসিক মিল রিপোর্ট") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.TotalMealRet.route)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )

                // 7. বাজার তালিকা
                NavigationDrawerItem(
                    label = { Text("মিল ও বাজার স্টেটমেন্ট") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.TotalMealList.route)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )




            }
        }
    ){

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ড্যাশবোর্ড") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.status_bar_green), // SAME as status bar
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    actions = {

                        // 🔔 Notification Icon
                        IconButton(onClick = {
                            // TODO: notification click action
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_notifications_24),
                                contentDescription = "Notifications"
                            )
                        }
                        IconButton(onClick = {
                            navController.navigate(Screen.Profile.route)
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.img_1),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)

                            )
                        }

                    },

                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {

                            Icon(
                                painter = painterResource(id = R.drawable.outline_menu_24),
                                contentDescription = null
                            )
                        }
                    }
                )
            },

            // 🔥 BOTTOM NAVIGATION
            bottomBar = {

                val routes = listOf(
                    Screen.dashboard.route,
                    Screen.Meal.route,
                    Screen.Users.route
                )

                val containerColor = Color(0xFF06420C)
                val selectedColor = Color(0xFF07A40C)
                val unselectedColor = Color(0xFFECEBE7)
                NavigationBar(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(containerColor),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    items.forEachIndexed { index, item ->
                        // এখানে routes[index] এর সাথে মিলানো হচ্ছে
                        val selected = currentRoute == routes[index]

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (selected) return@NavigationBarItem

                                navController.navigate(routes[index]) {
                                    // এটি প্রথম স্ক্রিনে ব্যাক করা নিশ্চিত করে
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        when (index) {
                                            0 -> R.drawable.baseline_home_24
                                            1 -> R.drawable.outline_menu_book_2_24
                                            else -> R.drawable.outline_person_24
                                        }
                                    ),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(text = item)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = selectedColor,
                                unselectedIconColor = unselectedColor,
                                selectedTextColor = selectedColor,
                                unselectedTextColor = unselectedColor,
                                indicatorColor = selectedColor.copy(alpha = 0.2f)
                            )
                        )
                    }

                }




            }


        ) { padding ->




                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color(0xFFF7F9FA))
                    ) {

                        // ================= MAIN UI =================
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.TopCenter
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                                    .padding(bottom = 80.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(10.dp)) // টপ স্পেস কমানো হয়েছে

                                // ================= CARD 1: আজকের বাজার =================
                                Card(
                                    onClick = { navController.navigate(Screen.Shdule.route) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 4.dp), // বাইরের মার্জিন কমানো হয়েছে
                                    shape = RoundedCornerShape(20.dp), // কর্নার রেডিয়াস কিছুটা শার্প করা হয়েছে
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                                ) {
                                    Column(modifier = Modifier.padding(9.dp)) { // ভেতরের প্যাডিং ২০ থেকে ১৪ করা হয়েছে

                                        // হেডার রো
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp) // সাইজ কমানো হয়েছে
                                                        .background(Color(0xFFE8F5E9), CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "🛒", fontSize = 14.sp)
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "আজকের বাজার",
                                                    color = Color(0xFF1B5E20),
                                                    style = MaterialTheme.typography.titleSmall.copy( // ফন্ট সাইজ ছোট করা হয়েছে
                                                        fontWeight = FontWeight.ExtraBold,
                                                        letterSpacing = 0.3.sp
                                                    )
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(50.dp),
                                                color = Color(0xFFE8F5E9),
                                            ) {
                                                Text(
                                                    text = "চলতি দায়িত্ব",
                                                    color = Color(0xFF2E7D32),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), // ছোট ফন্ট
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp) // চিপের সাইজ কমানো হয়েছে
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(5.dp))
                                        HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                                        Spacer(modifier = Modifier.height(5.dp))

                                        // --- ১. নাম ব্লক ---
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF8F9FA), RoundedCornerShape(10.dp))
                                                .padding(horizontal = 10.dp, vertical = 6.dp), // ছোট কমপ্যাক্ট প্যাডিং
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp) // আইকন ব্যাকগ্রাউন্ড ছোট করা হয়েছে
                                                    .background(
                                                        Color(0xFFE3F2FD),
                                                        RoundedCornerShape(8.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.outline_person_24),
                                                    contentDescription = null,
                                                    tint = Color(0xFF1E88E5),
                                                    modifier = Modifier.size(15.dp) // আইকন ছোট করা হয়েছে
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(7.dp))
                                            Column {
                                                Text(
                                                    text = "বাজারকারী",
                                                    color = Color.Gray,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = today?.name ?: "N/A",
                                                    color = Color(0xFF2D3748),
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                        }

                                        // --- ২. ইমেইল ব্লক ---
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.baseline_email_24),
                                                    contentDescription = null,
                                                    tint = Color(0xFFFB8C00),
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(text = "ইমেইল", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                                Text(text = today?.email ?: "N/A", color = Color(0xFF2D3748), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        // --- ৩. মোবাইল ব্লক ---
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.outline_settings_phone_24),
                                                    contentDescription = null,
                                                    tint = Color(0xFF4CAF50),
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(text = "মোবাইল", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                                Text(text = today?.number ?: "N/A", color = Color(0xFF2D3748), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // ডিউটি মেয়াদ বক্স (সাইজ ছোট ও স্লিক করা হয়েছে)
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFF4F6F8),
                                            border = BorderStroke(1.dp, Color(0xFFEAEAEA))
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = "📅  মেয়াদ: ",
                                                    color = Color(0xFF555555),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "${today?.start_date ?: ""} থেকে ${today?.end_date ?: ""}",
                                                    color = Color(0xFF2E7D32),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // ================= CARD 2: আজকের চলমান মিল =================
                                Card(
                                    onClick = { navController.navigate(Screen.MealsData.route) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {

                                        // হেডার রো
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .background(Color(0xFFFFF8E1), CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "🍱", fontSize = 14.sp)
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "আজকের চলমান মিল",
                                                    color = Color(0xFF1B5E20),
                                                    style = MaterialTheme.typography.titleSmall.copy(
                                                        fontWeight = FontWeight.ExtraBold,
                                                        letterSpacing = 0.3.sp
                                                    )
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(50.dp),
                                                color = Color(0xFFF5F5F5),
                                            ) {
                                                Text(
                                                    text = "${meals.firstOrNull()?.date ?: "No Date"}",
                                                    color = Color(0xFF616161),
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                                        Spacer(modifier = Modifier.height(10.dp))

                                        // --- দুপুরের মিল টাইল ---
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    brush = Brush.horizontalGradient(listOf(Color(0xFFFAFAFA), Color(0xFFF1F8E9))),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .border(1.dp, Color(0xFFEAEAEA), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "🍛", fontSize = 16.sp)
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(text = "দুপুরের মিল", color = Color(0xFF2D3748), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                }
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "${summary?.total_dupur ?: 0}", color = Color(0xFF2E7D32), fontSize = 16.sp, fontWeight = FontWeight.Black)
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text(text = "টি", color = Color.Gray, fontSize = 12.sp)
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // --- রাতের মিল টাইল ---
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    brush = Brush.horizontalGradient(listOf(Color(0xFFFAFAFA), Color(0xFFECEFF1))),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .border(1.dp, Color(0xFFEAEAEA), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "🌙", fontSize = 16.sp)
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(text = "রাতের মিল", color = Color(0xFF2D3748), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                                }
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "${summary?.total_rat ?: 0}", color = Color(0xFF37474F), fontSize = 16.sp, fontWeight = FontWeight.Black)
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text(text = "টি", color = Color.Gray, fontSize = 12.sp)
                                                }
                                            }
                                        }

//                                        Spacer(modifier = Modifier.height(14.dp))
                                        Spacer(modifier = Modifier.height(14.dp))
                                        HorizontalDivider(color = Color(0xFF020101), thickness = 1.dp)
                                        Spacer(modifier = Modifier.height(10.dp))
                                        // --- মোট মিল संख्या বক্স ---
                                        Surface(
                                            color = Color(0xFF1B5E20),
                                            shape = RoundedCornerShape(14.dp),
                                            modifier = Modifier.fillMaxWidth(),
                                            shadowElevation = 2.dp
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(26.dp)
                                                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(text = "📊", fontSize = 12.sp)
                                                    }
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = "মোট মিল সংখ্যা",
                                                        color = Color.White,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }

                                                Text(
                                                    text = "${summary?.total_counter ?: 0} টি",
                                                    color = Color(0xFFEEFF41),
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 🔴 FAB (মিনিমাল ও স্লিক করা হয়েছে)
                            ExtendedFloatingActionButton(
                                onClick = { navController.navigate(Screen.GroceryListScreen.route) },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = 20.dp, end = 20.dp),
                                shape = RoundedCornerShape(16.dp),
                                containerColor = Color(0xFF2E7D32),
                                contentColor = Color.White,
                                elevation = FloatingActionButtonDefaults.elevation(4.dp),
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_add_24),
                                        contentDescription = "Add",
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = "বাজার যোগ করুন",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )
                        }

                        // ================= LOADING STATE =================
                        if (loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = Color(0xFF2E7D32),
                                            strokeWidth = 3.dp,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "ডাটা লোড হচ্ছে...",
                                            color = Color(0xFF333333),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }



        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun loginui() {
    val navController = rememberNavController()
//    val viewModel = ScheduleViewModel()
    dashboardScreen(navController)
}