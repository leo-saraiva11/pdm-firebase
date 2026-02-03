package com.example.todolistfirebase.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistfirebase.data.AuthRepository
import com.example.todolistfirebase.data.TodoRepository
import com.example.todolistfirebase.domain.Todo
import com.example.todolistfirebase.navigation.AddEditRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ListUiEvent {
    data class Navigate(val route: Any) : ListUiEvent()
    data class ShowSnackbar(val message: String) : ListUiEvent()
}

class ListViewModel(
    private val todoRepository: TodoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiEvent = Channel<ListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    
    // Observa o usuário atual e busca suas tarefas
    val todos = authRepository.currentUser
        .flatMapLatest { userId ->
            if (userId != null) {
                todoRepository.getTodos(userId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAddClick() {
        viewModelScope.launch {
            _uiEvent.send(ListUiEvent.Navigate(AddEditRoute(null)))
        }
    }

    fun onTodoClick(todo: Todo) {
        viewModelScope.launch {
            _uiEvent.send(ListUiEvent.Navigate(AddEditRoute(todo.id)))
        }
    }

    fun onCompletedChange(todo: Todo, isCompleted: Boolean) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo.copy(isCompleted = isCompleted))
        }
    }

    fun onDeleteClick(todoId: String) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todoId)
        }
    }
    
    fun onLogoutClick() {
        authRepository.logout()
    }
}
