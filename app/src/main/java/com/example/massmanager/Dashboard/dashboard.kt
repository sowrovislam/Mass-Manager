package com.example.massmanager.Dashboard

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun dashboardScreen(navController: NavController, viewModel: ScheduleViewModel) {
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
                modifier = Modifier.fillMaxSize()
                    .background(color = colorResource(R.color.card_background))
            ) {






                // ================= MAIN UI =================
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.TopCenter
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            onClick = {
                                navController.navigate(Screen.Shdule.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            ),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {

                            Column(modifier = Modifier.padding(20.dp)) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🛒 আজকের বাজার",
                                        color = Color(0xFF113E15), // ডার্ক গ্রিন যা সহজে পড়া যায়
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )

                                    // ডেটটিকে একটি ছোট মিনিমালিস্ট চিপের মতো লুক দেওয়া হয়েছে
                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        color = Color(0xFFE1F0E3),
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text(
                                            text = "চলতি দায়িত্ব",
                                            color = Color(0xFF2E7D32),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Medium
                                            ),
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            )
                                        )
                                    }
                                }
                                HorizontalDivider(
                                    color = Color(0xFF2E7D32).copy(alpha = 0.1f),
                                    thickness = 1.2.dp,
                                    modifier = Modifier.padding(top =5.dp )
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_person_24),
                                        contentDescription = null,
                                        tint = Color(0xFF2E7D32),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "নাম: ${today?.name ?: "N/A"}",
                                        color = Color.Black
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_email_24),
                                        contentDescription = null,
                                        tint = Color(0xFF2E7D32),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("ইমেইল: ${today?.email ?: "N/A"}",
                                        color = Color.Black)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.outline_settings_phone_24),
                                        contentDescription = null,
                                        tint = Color(0xFF2E7D32),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("মোবাইল: ${today?.number ?: "N/A"}",
                                        color = Color.Black)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF2E7D32).copy(alpha = 0.1f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "📅 Date: ${today?.start_date ?: ""} → ${today?.end_date ?: ""}",
                                        color = Color(0xFF2E7D32),
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }


                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            onClick = {
                                navController.navigate(Screen.MealsData.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(24.dp), // একটু বেশি রাউন্ডেড করা হয়েছে মডার্ন লুকের জন্য
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF1F9F3) // আরও সফট এবং চোখের জন্য আরামদায়ক গ্রিন-হোয়াইট ব্যাকগ্রাউন্ড
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // অতিরিক্ত শ্যাডো কমিয়ে ২-৪ dp দেওয়া মডার্ন স্ট্যান্ডার্ড
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {

                                // ================= TITLE & DATE =================
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🛒 আজকের চলমান মিল",
                                        color = Color(0xFF113E15), // ডার্ক গ্রিন যা সহজে পড়া যায়
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )

                                    // ডেটটিকে একটি ছোট মিনিমালিস্ট চিপের মতো লুক দেওয়া হয়েছে
                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        color = Color(0xFFE1F0E3),
                                        modifier = Modifier.padding(start = 8.dp)
                                    ) {
                                        Text(
                                            text = "${meals.firstOrNull()?.date ?: "No Date"}",
                                            color = Color(0xFF2E7D32),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Medium
                                            ),
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                HorizontalDivider(
                                    color = Color(0xFF2E7D32).copy(alpha = 0.1f),
                                    thickness = 1.2.dp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // ================= VERTICAL MEAL ITEMS =================
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                                    // 🍛 DOPUR (Lunch)
                                    Surface(
                                        color = Color.White,
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "🍛 দুপুরের মিল",
                                                color = Color(0xFF388E3C),
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )

                                            Text(
                                                text = "${summary?.total_dupur?: 0}টি",
                                                color = Color(0xFF1B5E20),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }

                                    // 🌙 RAT (Dinner)
                                    Surface(
                                        color = Color.White,
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "🌙 রাতের মিল",
                                                color = Color(0xFF388E3C),
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )

                                            Text(
                                                text = "${summary?.total_rat?: 0} টি",
                                                color = Color(0xFF1B5E20),
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // ================= TOTAL MEAL BOX =================
                                // মোট মিল হাইলাইট করার জন্য আলাদা রঙের একটি প্রিমিয়াম কন্টেইনার
                                Surface(
                                    color = Color(0xFF2E7D32),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "📊 মোট মিল সংখ্যা",
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )

                                        Text(
                                            text = "${summary?.total_counter ?: 0} টি",
                                            color = Color(0xFFFFF176),
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        )
                                    }
                                }
                            }
                        }














                    }

                    // 🔴 FAB (Bottom End)
                    FloatingActionButton(
                        onClick = {
                          navController.navigate(Screen.GroceryListScreen.route)
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                end = 30.dp
                            ),
                        shape = RoundedCornerShape(16.dp),
                        containerColor = colorResource(R.color.primaryColor),
                        contentColor = Color.White
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_add_24),
                            contentDescription = "Add",
                            modifier = Modifier.size(24.dp)
                        )
                    }


                }

                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                CircularProgressIndicator(
                                    color = Color(0xFF2E7D32),
                                    strokeWidth = 3.dp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Loading...",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyMedium
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
    val viewModel = ScheduleViewModel()
    dashboardScreen(navController, viewModel)
}