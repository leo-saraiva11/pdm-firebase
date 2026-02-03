package com.example.todolistfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.todolistfirebase.data.AppContainer
import com.example.todolistfirebase.navigation.AppNavHost
import com.example.todolistfirebase.navigation.ListRoute
import com.example.todolistfirebase.navigation.LoginRoute
import com.example.todolistfirebase.ui.theme.TodoListFirebaseTheme
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Verificação simples de login inicial (pode ser melhorado com splash screen)
        val startDestination = runBlocking {
            if (AppContainer.authRepository.currentUser.first() != null) {
                ListRoute
            } else {
                LoginRoute
            }
        }

        setContent {
            TodoListFirebaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}
