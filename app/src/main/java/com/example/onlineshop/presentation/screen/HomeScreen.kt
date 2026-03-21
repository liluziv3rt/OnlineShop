package com.example.onlineshop.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.onlineshop.domain.model.Action
import com.example.onlineshop.domain.model.Category
import com.example.onlineshop.domain.model.Product
import com.example.onlineshop.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val products = viewModel.products
    val categories = viewModel.categories
    val actions = viewModel.actions
    val errorMessage = viewModel.errorMessage
    val showAllProducts by remember { derivedStateOf { viewModel.showAllProducts } }

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        Icon(Icons.Default.Menu, contentDescription = "Меню")
                    }
                    Text(
                        text = "Главная",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = {  }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Корзина")
                    }
                }
            }

            item {
                SearchBarWithFilter()
            }

            item {
                CategoryRow(
                    categories = categories,
                    selectedId = viewModel.selectedCategoryId,
                    onSelect = viewModel::selectCategory
                )
            }

            item {
                SectionHeader(
                    title = "Популярное",
                    showAll = showAllProducts,
                    onToggleShowAll = viewModel::toggleShowAllProducts
                )
            }

            val displayedProducts = if (showAllProducts) products else products.take(2)
            items(displayedProducts.chunked(2)) { chunk ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    chunk.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(
                                product = product,
                                onFav = { viewModel.toggleFav(product.id) },
                                onCart = { viewModel.toggleCart(product.id) }
                            )
                        }
                    }
                    if (chunk.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            if (actions.isNotEmpty()) {
                item {
                    Text(
                        text = "Акции",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    ActionItem(action = actions.first())
                }
            }

            if (errorMessage != null) {
                item {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SearchBarWithFilter() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text("Поиск") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = { /* фильтр */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Фильтр")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp)),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5)
        ),
        singleLine = true
    )
}

@Composable
fun CategoryRow(
    categories: List<Category>,
    selectedId: String?,
    onSelect: (String?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            val isSelected = category.id == selectedId
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) Color(0xFF03A9F4) else Color.LightGray.copy(alpha = 0.3f)
                    )
                    .clickable {
                        onSelect(if (category.id.isEmpty()) null else category.id)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category.title,
                    color = if (isSelected) Color.White else Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    showAll: Boolean,
    onToggleShowAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onToggleShowAll) {
            Text(
                text = if (showAll) "Свернуть" else "Все",
                color = Color(0xFF03A9F4)
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onFav: () -> Unit,
    onCart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onFav,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = if (product.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (product.isFavourite) Color.Red else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (product.isBestSeller) {
                Text(
                    text = "BEST SELLER",
                    color = Color(0xFF03A9F4),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = product.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )

            Text(
                text = "${product.cost} ₽",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onCart,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF03A9F4).copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = if (product.inCart) Icons.Default.ShoppingCart else Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(0xFF03A9F4)
                    )
                }
            }
        }
    }
}

@Composable
fun ActionItem(action: Action) {
    AsyncImage(
        model = action.imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
    )
}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("main", Icons.Default.Home),
        BottomNavItem("favorites", Icons.Default.FavoriteBorder),
        BottomNavItem("cart", Icons.Default.ShoppingCart),
        BottomNavItem("notifications", Icons.Default.Notifications),
        BottomNavItem("profile", Icons.Default.Person)
    )
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEachIndexed { index, item ->
            when (index) {
                2 -> {
                    Box(modifier = Modifier.weight(1f)) {
                        IconButton(
                            onClick = { navController.navigate(item.route) },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFF03A9F4))
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
                else -> {
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (currentRoute == item.route) Color(0xFF03A9F4) else Color.Gray
                            )
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    }
}

data class BottomNavItem(val route: String, val icon: ImageVector)