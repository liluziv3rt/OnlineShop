package com.example.onlineshop.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.onlineshop.domain.model.Action
import com.example.onlineshop.domain.model.Category
import com.example.onlineshop.domain.model.Product
import com.example.onlineshop.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val products = viewModel.products
    val categories = viewModel.categories
    val actions = viewModel.actions
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(Unit) {
        // load уже вызывается в init, но можно оставить для перезагрузки
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Главная", fontSize = 26.sp)
        Spacer(Modifier.height(12.dp))
        SearchBar()
        Spacer(Modifier.height(16.dp))
        ActionsRow(actions)
        Spacer(Modifier.height(16.dp))
        CategoryRow(
            categories = categories,
            selectedId = viewModel.selectedCategoryId,
            onSelect = viewModel::selectCategory
        )
        Spacer(Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onFav = { viewModel.toggleFav(product.id) },
                    onCart = { viewModel.toggleCart(product.id) }
                )
            }
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
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(Modifier.padding(8.dp)) {

            Box {

                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                IconButton(
                    onClick = onFav,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (product.isFavourite)
                            Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                }
            }

            if (product.isBestSeller) {
                Text(
                    "BEST SELLER",
                    color = Color.Blue,
                    fontSize = 12.sp
                )
            }

            Text(product.title)

            Text("${product.cost} ₽")

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = onCart) {
                    Icon(
                        imageVector = if (product.inCart)
                            Icons.Default.ShoppingCart
                        else Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
fun SearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("Поиск") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun BottomBar(navController: NavController) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("favorites") },
            icon = { Icon(Icons.Default.FavoriteBorder, null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("cart") },
            icon = { Icon(Icons.Default.ShoppingCart, null) }
        )
    }
}

@Composable
fun CategoryRow(
    categories: List<Category>,
    selectedId: String?,
    onSelect: (String?) -> Unit
) {

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        items(categories) { category ->

            val isSelected = category.id == selectedId

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) Color.Black else Color.LightGray
                    )
                    .clickable {
                        onSelect(
                            if (category.id.isEmpty()) null else category.id
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category.title,
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun ActionsRow(actions: List<Action>) {

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

        items(actions) { action ->

            AsyncImage(
                model = action.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 300.dp, height = 120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}