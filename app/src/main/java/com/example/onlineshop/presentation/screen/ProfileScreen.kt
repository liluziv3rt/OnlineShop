package com.example.onlineshop.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userProfileState by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

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

                        IconButton(onClick = { navController.navigate("edit_profile") }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = Color.Black
                            )
                        }
                    }
                }

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


                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val barcodePattern = remember(userProfile?.id) {
                                generateBarcodePattern(userProfile?.id ?: "123456789")
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                barcodePattern.forEach { width ->
                                    Box(
                                        modifier = Modifier
                                            .width(width.dp)
                                            .fillMaxHeight()
                                            .background(Color.Black)
                                    )
                                    Spacer(modifier = Modifier.width(1.dp))
                                }
                            }

                            Text(
                                text = userProfile?.id?.takeLast(8)?.chunked(4)?.joinToString(" ") ?: "0000 0000",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }

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


private fun generateBarcodePattern(id: String): List<Int> {
    val pattern = mutableListOf<Int>()
    val hash = id.hashCode().absoluteValue
    val hashString = hash.toString() + id.takeLast(4)

    for (char in hashString) {
        val digit = char.digitToIntOrNull() ?: 0
        when (digit) {
            0 -> pattern.addAll(listOf(2, 1, 2, 1, 2))
            1 -> pattern.addAll(listOf(1, 2, 1, 2, 1))
            2 -> pattern.addAll(listOf(2, 2, 1, 1, 2))
            3 -> pattern.addAll(listOf(1, 1, 2, 2, 1))
            4 -> pattern.addAll(listOf(2, 1, 1, 2, 2))
            5 -> pattern.addAll(listOf(1, 2, 2, 1, 1))
            6 -> pattern.addAll(listOf(2, 2, 2, 1, 1))
            7 -> pattern.addAll(listOf(1, 1, 1, 2, 2))
            8 -> pattern.addAll(listOf(2, 1, 2, 2, 1))
            9 -> pattern.addAll(listOf(1, 2, 1, 1, 2))
        }
    }

    return pattern
}