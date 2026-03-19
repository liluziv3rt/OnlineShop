package com.example.onlineshop.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.anotherexamrepeat.presentation.screen.SignInScreen
import com.example.onlineshop.presentation.screen.HomeScreen
import com.example.onlineshop.presentation.screen.MenuScreen
import com.example.onlineshop.presentation.screen.ProfileScreen
import com.example.onlineshop.presentation.screens.SignUpScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "signIn") {
        composable("signIn") {
            SignInScreen(navController)
        }
        composable("signUp") {
            SignUpScreen(navController = navController)
        }
        composable("main") {
            HomeScreen(navController = navController)
        }
        composable("menu") {
            MenuScreen(navController = navController)
        }

        composable("profile") {
            ProfileScreen(navController = navController)
        }


    }
}