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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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

    private val _uiEvent = Channel<AddEditUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
                        _uiEvent.send(AddEditUiEvent.ShowSnackbar("Error loading todo"))
                    }
                    Result.Loading -> Unit
                }
            }
        }
    }

    /** Gemini - início
     * Prompt: Add KDoc and safely handle null user in onSaveClick
     */
    fun onTitleChange(newTitle: String) {
        title = newTitle
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }

    fun onSaveClick() {
        if (title.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send(AddEditUiEvent.ShowSnackbar("Title cannot be empty"))
            }
            return
        }

        viewModelScope.launch {
            try {
                // Ensure we get the current user safely
                val userId = authRepository.getUserId()
                if (userId == null) {
                    _uiEvent.send(AddEditUiEvent.ShowSnackbar("User not logged in. Cannot save."))
                    return@launch
                }
                
                val todo = Todo(
                    id = todoId ?: "", // ID will be ignored on add
                    title = title,
                    description = description.takeIf { it.isNotBlank() },
                    userId = userId
                )
    
                val result = if (todoId == null) {
                    todoRepository.addTodo(todo)
                } else {
                    todoRepository.updateTodo(todo)
                }
    
                when (result) {
                    is Result.Success -> {
                        _uiEvent.send(AddEditUiEvent.NavigateBack)
                    }
                    is Result.Error -> {
                        _uiEvent.send(AddEditUiEvent.ShowSnackbar("Error saving todo: ${result.exception.message}"))
                    }
                    Result.Loading -> Unit
                }
            } catch (e: Exception) {
                 _uiEvent.send(AddEditUiEvent.ShowSnackbar("Unexpected error: ${e.message}"))
            }
        }
    }
    /** Gemini - final */
}
