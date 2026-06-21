package com.example.massmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.massmanager.Api_Otp.Data_Class.MealItem
import com.example.massmanager.Api_Otp.Data_Class.SessionManager
import com.example.massmanager.R
import com.example.massmanager.ViewModel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsData(
    navController: NavController,
    viewModel: ScheduleViewModel
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // States from ViewModel
    val meals by viewModel.meals.collectAsState()
    val loadingMeals by viewModel.loadingMeals.collectAsState()

    LaunchedEffect(Unit) {
        val userId = sessionManager.getUserId()
        viewModel.loadMeals(userId.toString())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("চলমান মিল", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.status_bar_green), // SAME as status bar
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
            if (loadingMeals) {
                // ✅ Modern Progress Bar Layout
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading meals...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else if (meals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No meals found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(meals) { meal ->
                        MealItemCard(meal)
                    }
                }
            }
        }
    }
}

@Composable
fun MealItemCard(meal: MealItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Avatar + Profile Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_person_24),
                        contentDescription = "User Profile",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = meal.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            // Info Row: Date & Counter Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24), // Swap with calendar resource if available
                        contentDescription = "Date Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = meal.date,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                SuggestionChip(
                    onClick = { },
                    label = {
                        Text(
                            text = "Total: ${meal.counter}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ Enhanced Session Status Badges (Dupur & Rat)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Dupur: ${meal.isDupur}",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Rat: ${meal.isRat}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RowScope.MealSessionStatusBadge(label: String, value: String) {
    // Check if the meal is active/turned on for this session
    val isActive = value.equals("yes", ignoreCase = true) || value.equals("true", ignoreCase = true) || value == "1"

    val containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .weight(1f)
            .background(containerColor, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            color = contentColor
        )
    }
}