package com.example.anotherexamrepeat.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val accentColor = Color(0xFF03A9F4)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Привет!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Заполните Свои Данные",
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = loginRequest.email,
                onValueChange = { viewModel.updateLoginRequest(loginRequest.copy(email = it)) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = accentColor
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = loginRequest.password,
                onValueChange = { viewModel.updateLoginRequest(loginRequest.copy(password = it)) },
                label = { Text("Пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = accentColor
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {  },
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = "Восстановить",
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            when (resultState) {
                is ResultState.Loading -> {
                    CircularProgressIndicator(color = accentColor)
                }
                is ResultState.Error -> {
                    Text(
                        text = (resultState as ResultState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                    Button(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text("Войти", color = Color.White)
                    }
                }
                is ResultState.Success -> {
                    Text(
                        text = "Вход выполнен!",
                        color = accentColor,
                        modifier = Modifier.padding(8.dp)
                    )
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = accentColor)
                }
                else -> {
                    Button(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = loginRequest.email.isNotBlank() && loginRequest.password.isNotBlank(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            disabledContainerColor = accentColor.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("Войти", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = { navController.navigate("signUp") },
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Вы впервые? Создать",
                    color = accentColor
                )
            }
        }
    }
}