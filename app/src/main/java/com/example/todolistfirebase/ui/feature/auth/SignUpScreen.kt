package com.example.todolistfirebase.ui.feature.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistfirebase.data.AppContainer
import com.example.todolistfirebase.domain.Result
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val viewModel = viewModel<AuthViewModel> {
        AuthViewModel(AppContainer.authRepository)
    }
    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authState) {
        when (val result = authState) {
            is Result.Success -> {
                viewModel.resetState()
                onSignUpSuccess()
            }
            is Result.Error -> {
                errorMessage = when (result.exception) {
                    is FirebaseAuthWeakPasswordException -> "A senha deve ter no mínimo 6 caracteres."
                    is FirebaseAuthUserCollisionException -> "Este e-mail já está em uso."
                    else -> "Cadastro falhou: ${result.exception.message}"
                }
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Criar Conta", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it
                errorMessage = null
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                errorMessage = null
            },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                errorMessage = null 
            },
            label = { Text("Confirmar Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Preencha todos os campos."
                } else if (password != confirmPassword) {
                    errorMessage = "As senhas não conferem."
                } else {
                    viewModel.signUp(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is Result.Loading
        ) {
            if (authState is Result.Loading) {
                CircularProgressIndicator()
            } else {
                Text("Cadastrar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Já tem uma conta? Faça login",
            modifier = Modifier.clickable { onNavigateToLogin() },
            color = MaterialTheme.colorScheme.primary
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
