package com.example.onlineshop.presentation.screens

import androidx.compose.foundation.clickable
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
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val consentGiven by viewModel.consentGiven.collectAsState()

    var showPassword by remember { mutableStateOf(false) }

    // Проверка, все ли поля заполнены и галочка нажата
    val isFormValid = remember(email, password, fullName, consentGiven) {
        email.isNotBlank() &&
                password.isNotBlank() &&
                password.length >= 6 &&
                fullName.isNotBlank() &&
                fullName.contains(" ") && // Проверяем, что есть пробел (имя и фамилия)
                consentGiven
    }

    LaunchedEffect(resultState) {
        if (resultState is ResultState.Success) {
            delay(2000)
            navController.navigate("signInScreen") {
                popUpTo("signUpScreen") { inclusive = true }
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
                text = "Регистрация",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Имя и фамилия (теперь сверху)
            OutlinedTextField(
                value = fullName,
                onValueChange = { viewModel.updateFullName(it) },
                label = { Text("Имя и фамилия") },
                placeholder = { Text("Иван Петров") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = resultState is ResultState.Error && fullName.isBlank(),
                supportingText = {
                    if (fullName.isNotBlank() && !fullName.contains(" ")) {
                        Text(
                            text = "Введите имя и фамилию через пробел",
                            color = Color.Red,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Email
            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = resultState is ResultState.Error && email.isBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Пароль
            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Пароль") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Text(if (showPassword) "🙈" else "👁️")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = resultState is ResultState.Error && password.length < 6,
                supportingText = {
                    if (password.isNotBlank() && password.length < 6) {
                        Text(
                            text = "Пароль должен быть не менее 6 символов",
                            color = Color.Red,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Чекбокс согласия
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.updateConsent(!consentGiven) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = consentGiven,
                    onCheckedChange = { viewModel.updateConsent(it) }
                )
                Text(
                    text = "Даю согласие на обработку персональных данных",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка регистрации и статусы
            when (val state = resultState) {
                is ResultState.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )

                    Button(
                        onClick = { viewModel.signUp() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid  // Кнопка активна только при валидной форме
                    ) {
                        Text("Зарегистрироваться")
                    }
                }
                is ResultState.Success -> {
                    Text(
                        text = state.message,
                        color = Color.Green,
                        modifier = Modifier.padding(8.dp)
                    )
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                ResultState.Init -> {
                    Button(
                        onClick = { viewModel.signUp() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid  // Кнопка активна только при валидной форме
                    ) {
                        Text("Зарегистрироваться")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ссылка на вход
            TextButton(
                onClick = {
                    navController.navigate("signInScreen") {
                        popUpTo("signUpScreen") { inclusive = true }
                    }
                }
            ) {
                Text("Уже есть аккаунт? Войти")
            }
        }
    }
}