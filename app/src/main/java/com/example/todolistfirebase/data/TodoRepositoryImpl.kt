package com.example.todolistfirebase.data

import com.example.todolistfirebase.domain.Result
import com.example.todolistfirebase.domain.Todo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TodoRepositoryImpl(
    private val firestore: FirebaseFirestore
) : TodoRepository {

    private val todoCollection = firestore.collection("todos")

    override fun getTodos(userId: String): Flow<List<Todo>> = callbackFlow {
        val listener = todoCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val todos = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        val id = doc.id
                        val title = doc.getString("title") ?: ""
                        val description = doc.getString("description")
                        // Handle potential naming mismatch for boolean field
                        val isCompleted = doc.getBoolean("isCompleted") ?: doc.getBoolean("completed") ?: false
                        val userId = doc.getString("userId") ?: ""
                        
                        Todo(
                            id = id,
                            title = title,
                            description = description,
                            isCompleted = isCompleted,
                            userId = userId
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                } ?: emptyList()
                trySend(todos)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addTodo(todo: Todo): Result<Unit> {
        return try {
            val todoMap = hashMapOf(
                "title" to todo.title,
                "description" to todo.description,
                "isCompleted" to todo.isCompleted,
                "userId" to todo.userId
            )
            todoCollection.add(todoMap).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateTodo(todo: Todo): Result<Unit> {
        return try {
            val todoMap = hashMapOf(
                "title" to todo.title,
                "description" to todo.description,
                "isCompleted" to todo.isCompleted,
                "userId" to todo.userId
            )
            todoCollection.document(todo.id).set(todoMap).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteTodo(todoId: String): Result<Unit> {
        return try {
            todoCollection.document(todoId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getTodoById(todoId: String): Result<Todo?> {
        return try {
            val doc = todoCollection.document(todoId).get().await()
            if (doc.exists()) {
                val id = doc.id
                val title = doc.getString("title") ?: ""
                val description = doc.getString("description")
                val isCompleted = doc.getBoolean("isCompleted") ?: doc.getBoolean("completed") ?: false
                val userId = doc.getString("userId") ?: ""
                
                Result.Success(Todo(id, title, description, isCompleted, userId))
            } else {
                Result.Success(null)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
