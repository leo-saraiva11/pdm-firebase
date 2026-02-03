package com.example.todolistfirebase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.todolistfirebase.ui.feature.addedit.AddEditScreen
import com.example.todolistfirebase.ui.feature.auth.LoginScreen
import com.example.todolistfirebase.ui.feature.auth.SignUpScreen
import com.example.todolistfirebase.ui.feature.list.ListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any = LoginRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(ListRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(SignUpRoute)
                }
            )
        }

        composable<SignUpRoute> {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(ListRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable<ListRoute> {
            ListScreen(
                onNavigateToAddEdit = { todoId ->
                    navController.navigate(AddEditRoute(id = todoId))
                },
                onLogout = {
                    navController.navigate(LoginRoute) {
                        popUpTo(ListRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<AddEditRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<AddEditRoute>()
            AddEditScreen(
                todoId = route.id,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
