package com.example.onlineshop.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val user by viewModel.userProfile.collectAsState()
    val currentUser = user

    // Светло-голубой фон
    val backgroundColor = Color(0xFFE6F3FF) // Чуть светлее, как на макете

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхний отступ
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }

            // Большая аватарка
            item {
                if (currentUser?.photo != null) {
                    AsyncImage(
                        model = currentUser.photo,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Имя и фамилия
            item {
                Text(
                    text = listOfNotNull(currentUser?.firstName, currentUser?.lastName)
                        .joinToString(" ")
                        .ifBlank { "Пользователь" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(32.dp))
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

            items(menuItems) { item ->
                ListItem(
                    headlineContent = {
                        Text(
                            text = item.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (item.title) {
                                "Профиль" -> navController.navigate("profile")
                                "Корзина" -> navController.navigate("cart")
                                "Избранное" -> navController.navigate("favorites")
                                "Заказы" -> navController.navigate("orders")
                                "Уведомления" -> navController.navigate("notifications")
                                "Настройки" -> navController.navigate("settings")
                            }
                        },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
            }

            // Дополнительный отступ перед кнопкой выхода
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Кнопка выхода
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = "Выйти",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Red,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 8.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.logout()
                            navController.navigate("signIn") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
            }


        }
    }
}

data class MenuItem(val title: String, val icon: ImageVector)