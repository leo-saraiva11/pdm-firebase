package com.example.todolistfirebase.data

import com.example.todolistfirebase.domain.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<String?> // ID do usuário logado ou null
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    fun logout()
}
