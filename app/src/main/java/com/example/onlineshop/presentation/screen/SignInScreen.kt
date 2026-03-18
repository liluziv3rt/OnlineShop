package com.example.anotherexamrepeat.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onlineshop.presentation.model.ResultState
import com.example.onlineshop.presentation.viewmodel.SignInViewModel
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val resultState by viewModel.resultState.collectAsState()
    val loginRequest by viewModel.loginRequest.collectAsState()

    LaunchedEffect(resultState) {
        if (resultState is ResultState.Success) {
            delay(1000)
            navController.navigate("main") {
                popUpTo("signIn") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = loginRequest.email,
                onValueChange = { viewModel.updateLoginRequest(loginRequest.copy(email = it)) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = loginRequest.password,
                onValueChange = { viewModel.updateLoginRequest(loginRequest.copy(password = it)) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (resultState) {
                is ResultState.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultState.Error -> {
                    Text(
                        text = (resultState as ResultState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )

                    Button(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign In")
                    }
                }
                is ResultState.Success -> {
                    Text(
                        text = "Login successful!",
                        color = Color.Green,
                        modifier = Modifier.padding(8.dp)
                    )
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                else -> {
                    Button(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = loginRequest.email.isNotBlank() && loginRequest.password.isNotBlank()
                    ) {
                        Text("Sign In")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ссылка на регистрацию
            TextButton(
                onClick = {
                    navController.navigate("signUp")
                }
            ) {
                Text("Don't have an account? Sign Up")
            }
        }
    }
}