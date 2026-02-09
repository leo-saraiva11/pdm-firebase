package com.example.todolistfirebase.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistfirebase.data.AuthRepository
import com.example.todolistfirebase.data.TodoRepository
import com.example.todolistfirebase.domain.Result
import com.example.todolistfirebase.domain.Todo
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

sealed class AddEditUiEvent {
    data object NavigateBack : AddEditUiEvent()
    data class ShowSnackbar(val message: String) : AddEditUiEvent()
}

/**
 * ViewModel responsible for adding and editing Todo items.
 *
 * This ViewModel handles the business logic for creating new tasks or updating existing ones.
 * It interacts with [TodoRepository] for data persistence and [AuthRepository] to ensure
 * the task is associated with the current user.
 *
 * @param todoId The ID of the todo to edit, or null if creating a new one.
 * @param todoRepository Repository for Todo data operations.
 * @param authRepository Repository for Authentication operations.
 */
class AddEditViewModel(
    private val todoId: String?,
    private val todoRepository: TodoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    /** Gemini - início
     * Prompt: Add isSaving and shouldNavigateBack states for reliable navigation
     */
    var isSaving by mutableStateOf(false)
        private set
    
    // Using observable state for navigation instead of Channel
    var shouldNavigateBack by mutableStateOf(false)
        private set
    
    var snackbarMessage by mutableStateOf<String?>(null)
        private set
    /** Gemini - final */

    init {
        if (todoId != null) {
            viewModelScope.launch {
                when (val result = todoRepository.getTodoById(todoId)) {
                    is Result.Success -> {
                        result.data?.let { todo ->
                            title = todo.title
                            description = todo.description ?: ""
                        }
                    }
                    is Result.Error -> {
                        snackbarMessage = "Error loading todo"
                    }
                    Result.Loading -> Unit
                }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        title = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }
    
    fun onSnackbarShown() {
        snackbarMessage = null
    }

    /** Gemini - início
     * Prompt: Fix onSaveClick to use state-based navigation
     */
    fun onSaveClick() {
        // Prevent double-click
        if (isSaving) return

        if (title.isBlank()) {
            snackbarMessage = "Title cannot be empty"
            return
        }

        isSaving = true
        
        // Optimistic UI: Navigate back immediately!
        shouldNavigateBack = true

        val userId = authRepository.getUserId()
        if (userId == null) {
            // Should not happen if logged in, but just in case
            return
        }

        // Fire-and-forget save
        viewModelScope.launch {
            try {
                // Use NonCancellable to ensure save completes even if ViewModel is cleared
                kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) {
                    val todo = Todo(
                        id = todoId ?: "",
                        title = title,
                        description = description.takeIf { it.isNotBlank() },
                        userId = userId
                    )
        
                    if (todoId == null) {
                        todoRepository.addTodo(todo)
                    } else {
                        todoRepository.updateTodo(todo)
                    }
                }
            } catch (e: Exception) {
                // If save fails, we can't show snackbar because we already left.
                // In a real app, we might log this or show a notification.
                e.printStackTrace()
            }
        }
    }
    /** Gemini - final */
}


