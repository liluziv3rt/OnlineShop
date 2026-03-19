package com.example.onlineshop.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.onlineshop.presentation.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val profile = userProfile
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Меню") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Шапка с аватаром и именем
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (profile?.photo != null) {
                        AsyncImage(
                            model = profile.photo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        val user = userProfile // это User? из сессии
                        val fullName = listOfNotNull(user?.firstName, user?.lastName)
                            .joinToString(" ")
                            .ifBlank { "Пользователь" }

                        Text(
                            text = fullName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Divider()
            }

            // Пункты меню
            val menuItems = listOf(
                MenuItem("Профиль", Icons.Default.Person),
                MenuItem("Корзина", Icons.Default.ShoppingCart),
                MenuItem("Избранное", Icons.Default.FavoriteBorder),
                MenuItem("Заказы", Icons.Default.List),
                MenuItem("Уведомления", Icons.Default.Notifications),
                MenuItem("Настройки", Icons.Default.Settings)
            )
            items(menuItems.size) { index ->
                val item = menuItems[index]
                ListItem(
                    headlineContent = { Text(item.title) },
                    leadingContent = { Icon(item.icon, null) },
                    modifier = Modifier.clickable {
                        // Здесь можно добавить навигацию позже
                        // Например, navController.navigate(item.route)
                    }
                )
            }

            // Выход
            item {
                Divider()
                ListItem(
                    headlineContent = { Text("Выйти") },
                    leadingContent = { Icon(Icons.Default.ExitToApp, null) },
                    modifier = Modifier.clickable {
                        viewModel.logout()
                        navController.navigate("signIn") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

data class MenuItem(val title: String, val icon: ImageVector)