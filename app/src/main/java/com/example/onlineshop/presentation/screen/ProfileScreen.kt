package com.example.onlineshop.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.onlineshop.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userProfileState by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Сохраняем в локальную переменную
    val userProfile = userProfileState

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                // Верхняя панель с кнопками
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { navController.navigate("menu") }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Меню",
                                tint = Color.Black
                            )
                        }

                        Text(
                            text = "Профиль",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        IconButton(onClick = { /* TODO: редактирование */ }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = Color.Black
                            )
                        }
                    }
                }

                // Аватар
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (userProfile?.photoUrl != null) {
                            AsyncImage(
                                model = userProfile.photoUrl,
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
                    }
                }

                // Имя и фамилия
                item {
                    Text(
                        text = listOfNotNull(userProfile?.firstName, userProfile?.lastName)
                            .joinToString(" ")
                            .ifBlank { "Пользователь" },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }

                // Поля профиля
                item {
                    ProfileField(
                        label = "Имя",
                        value = userProfile?.firstName ?: "Не указано"
                    )
                }

                item {
                    ProfileField(
                        label = "Фамилия",
                        value = userProfile?.lastName ?: "Не указано"
                    )
                }

                item {
                    ProfileField(
                        label = "Адрес",
                        value = userProfile?.address ?: "Не указано"
                    )
                }

                item {
                    ProfileField(
                        label = "Телефон",
                        value = userProfile?.phone ?: "Не указано"
                    )
                }

                // Нижний отступ
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 4.dp)
        )
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}