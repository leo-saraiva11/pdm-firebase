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
            val userId = authRepository.currentUser.first() ?: return@launch
            
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
        }
    }
}
