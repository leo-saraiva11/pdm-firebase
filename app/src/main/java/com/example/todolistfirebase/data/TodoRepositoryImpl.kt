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
                    doc.toObject<Todo>()?.copy(id = doc.id)
                } ?: emptyList()
                trySend(todos)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addTodo(todo: Todo): Result<Unit> {
        return try {
            todoCollection.add(todo).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateTodo(todo: Todo): Result<Unit> {
        return try {
            todoCollection.document(todo.id).set(todo).await()
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
            Result.Success(doc.toObject<Todo>()?.copy(id = doc.id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
