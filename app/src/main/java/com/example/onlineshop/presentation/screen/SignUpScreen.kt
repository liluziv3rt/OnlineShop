// presentation/screens/SignUpScreen.kt
package com.example.onlineshop.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onlineshop.presentation.model.ResultState
import com.example.onlineshop.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val resultState by viewModel.resultState.collectAsState()
    val registerRequest by viewModel.registerRequest.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    // Обработка успешной регистрации - переход на экран входа
    LaunchedEffect(resultState) {
        if (resultState is ResultState.Success) {
            delay(2000)
            navController.navigate("signIn") {
                popUpTo("signUp") { inclusive = true }
            }
            viewModel.resetState()
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
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поля для имени и фамилии (опционально)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = registerRequest.firstName ?: "",
                    onValueChange = { viewModel.updateFirstName(it) },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = registerRequest.lastName ?: "",
                    onValueChange = { viewModel.updateLastName(it) },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = registerRequest.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = resultState is ResultState.Error
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = registerRequest.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Text(if (showPassword) "🙈" else "👁️")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = resultState is ResultState.Error
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Text(if (showConfirmPassword) "🙈" else "👁️")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = resultState is ResultState.Error
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
                        onClick = { viewModel.signUp() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Up")
                    }
                }
                is ResultState.Success -> {
                    Text(
                        text = (resultState as ResultState.Success).message,
                        color = Color.Green,
                        modifier = Modifier.padding(8.dp)
                    )
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                else -> {
                    Button(
                        onClick = { viewModel.signUp() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.validateForm()
                    ) {
                        Text("Sign Up")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ссылка на вход
            TextButton(
                onClick = {
                    navController.navigate("signIn") {
                        popUpTo("signUp") { inclusive = true }
                    }
                }
            ) {
                Text("Already have an account? Sign In")
            }
        }
    }
}