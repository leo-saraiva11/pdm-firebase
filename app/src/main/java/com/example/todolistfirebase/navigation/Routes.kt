package com.example.todolistfirebase.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object SignUpRoute

@Serializable
object ListRoute

@Serializable
data class AddEditRoute(val id: String? = null)
