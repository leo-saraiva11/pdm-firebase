package com.example.todolistfirebase.domain

data class Todo(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val isCompleted: Boolean = false,
    val userId: String = ""
)
