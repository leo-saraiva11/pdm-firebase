package com.example.todolistfirebase.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AppContainer {
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(FirebaseAuth.getInstance())
    }

    val todoRepository: TodoRepository by lazy {
        TodoRepositoryImpl(FirebaseFirestore.getInstance())
    }
}
