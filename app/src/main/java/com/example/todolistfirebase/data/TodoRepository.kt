package com.example.todolistfirebase.data

import com.example.todolistfirebase.domain.Result
import com.example.todolistfirebase.domain.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(userId: String): Flow<List<Todo>>
    suspend fun addTodo(todo: Todo): Result<Unit>
    suspend fun updateTodo(todo: Todo): Result<Unit>
    suspend fun deleteTodo(todoId: String): Result<Unit>
    suspend fun getTodoById(todoId: String): Result<Todo?>
}
