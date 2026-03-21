package com.example.onlineshop.presentation.screens

import android.hardware.lights.LightState
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val accentColor = Color(0xFF03A9F4)

    val isFormValid = remember(email, password, fullName, consentGiven) {
        email.isNotBlank() && password.length >= 6 && fullName.isNotBlank() && consentGiven
    }

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
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Регистрация",
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
                value = fullName,
                onValueChange = { viewModel.updateFullName(it) },
                label = { Text("Ваше имя") },
                singleLine = true,
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
                value = email,
                onValueChange = { viewModel.updateEmail(it) },
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
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = accentColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.updateConsent(!consentGiven) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = consentGiven,
                    onCheckedChange = { viewModel.updateConsent(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = accentColor,
                        uncheckedColor = Color.Gray
                    )
                )
                Text(
                    text = "Даю согласие на обработку персональных данных",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = resultState) {
                is ResultState.Loading -> {
                    CircularProgressIndicator(color = accentColor)
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
                        enabled = isFormValid,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            disabledContainerColor = accentColor.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("Зарегистрироваться", color = Color.White)
                    }
                }
                is ResultState.Success -> {
                    Text(
                        text = state.message,
                        color = accentColor,
                        modifier = Modifier.padding(8.dp)
                    )
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = accentColor)
                }
                ResultState.Init -> {
                    Button(
                        onClick = { viewModel.signUp() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFormValid,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            disabledContainerColor = accentColor.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("Зарегистрироваться", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = { navController.navigate("signIn") },
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Есть аккаунт? Войти",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}