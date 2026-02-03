package com.example.todolistfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.todolistfirebase.data.AppContainer
import com.example.todolistfirebase.navigation.AppNavHost
import com.example.todolistfirebase.navigation.ListRoute
import com.example.todolistfirebase.navigation.LoginRoute
import com.example.todolistfirebase.ui.theme.TodoListFirebaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            TodoListFirebaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Observamos o fluxo do usuário atual. Usamos uma String especial "loading" para o estado inicial.
                    val currentUser by AppContainer.authRepository.currentUser.collectAsState(initial = "loading")

                    when (currentUser) {
                        "loading" -> {
                            // Enquanto o Firebase decide se há alguém logado
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        null -> {
                            // Ninguém logado -> Vai para Login
                            val navController = rememberNavController()
                            AppNavHost(
                                navController = navController,
                                startDestination = LoginRoute
                            )
                        }
                        else -> {
                            // Usuário logado -> Vai direto para a lista
                            val navController = rememberNavController()
                            AppNavHost(
                                navController = navController,
                                startDestination = ListRoute
                            )
                        }
                    }
                }
            }
        }
    }
}
