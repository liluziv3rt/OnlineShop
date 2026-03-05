package com.example.anotherexamrepeat.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.onlineshop.presentation.model.ResultState
import com.example.onlineshop.presentation.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
){

    val resultState = viewModel.resultState.collectAsState()

    val loginRequest = viewModel.loginRequest.collectAsState()

    Box(modifier = Modifier.fillMaxSize()){

        Column(modifier = Modifier.align(Alignment.Center)) {

            OutlinedTextField(
                value = loginRequest.value.email,
                onValueChange = {
                    viewModel.updateLoginRequest(
                        viewModel.loginRequest.value.copy(
                            email = it
                        )
                    )
                },
                label = {
                    Text(text = "Email")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = loginRequest.value.password,
                onValueChange = {
                    viewModel.updateLoginRequest(
                        viewModel.loginRequest.value.copy(
                            password = it
                        )
                    )
                },
                label = {
                    Text(text = "Пароль")
                },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            StateButton(
                label = "Войти",
                onClick = {
                    viewModel.signIn()
                },
                resultState = resultState.value,
                onSuccess = {
                    navController.navigate("listBookScreen")
                }
            )
        }

    }
}

@Composable
fun StateButton(
    label: String,
    onClick:() -> Unit,
    resultState: ResultState,
    onSuccess:(() -> Unit)? = null
){

    when(resultState){
        is ResultState.Error -> {

            Button(onClick = onClick)
            {
                Text(text = label)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = resultState.message, color = Color.Red)

        }
        ResultState.Init -> {
            Button(onClick = onClick)
            {
                Text(text = label)
            }
        }
        ResultState.Loading -> {
            CircularProgressIndicator()
        }
        is ResultState.Success -> {
            if(onSuccess == null){
                Button(onClick = onClick)
                {
                    Text(text = label)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = resultState.message, color = Color.Green)
            }else{
                onSuccess()
            }
        }
    }

}