package com.example.massmanager.Dashboard

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.massmanager.ViewModel.LoginViewModel
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.Login_File.LoginScreen
import com.example.massmanager.Navigation.Screen
import com.example.massmanager.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dashboardScreen(navController: NavController,viewModel: LoginViewModel) {

    val context = LocalContext.current
    val session = SessionManager(context)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sessionManager = SessionManager(context)
    sessionManager.getUserId()




    var selectedIndex by remember { mutableStateOf(0) }

    val items = listOf("Home", "Daily Meal", "User")

    ModalNavigationDrawer(
        drawerState = drawerState,

        // 🔥 DRAWER MENU
        drawerContent = {
            ModalDrawerSheet {

                Text(
                    text = "Mass Manager",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                HorizontalDivider()
                Spacer(modifier = Modifier.height(40.dp))

                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = true,
                    onClick = { },
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                )



                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        session.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Daly mill") },
                    selected = false,
                    onClick = {
                        session.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
















            }
        }
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
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

                NavigationBar(modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                ) {

                    items.forEachIndexed { index, item ->

                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },

                            icon = {
                                when (index) {
                                    0 -> Icon(
                                        painter = painterResource(id = R.drawable.baseline_home_24),
                                        contentDescription = null)

                                    1 -> Icon(
                                        painter = painterResource(id = R.drawable.outline_menu_book_2_24),
                                        contentDescription = null

                                    )

                                    else -> Icon(
                                        painter = painterResource(id = R.drawable.outline_person_24),
                                        contentDescription = null
                                    )
                                }
                            },

                            label = { Text(item) }
                        )
                    }
                }
            }

        ) { padding ->

            // 🔥 SCREEN CONTENT
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                   val id= sessionManager.getUserId()


                    when (selectedIndex) {
                        0 ->
                            Text("Home Screen")

                        1 -> Text("Profile Screen")
                        2 ->  navController.navigate(Screen.Users.route)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            session.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Logout")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("I Love You ❤️${sessionManager.getUserId()}")

                    Text("I Love You ❤️${sessionManager.getUserName()}")
                }
            }
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun loginui() {
    val navController = rememberNavController()
    val viewModel= LoginViewModel()
    dashboardScreen(navController,viewModel)
}