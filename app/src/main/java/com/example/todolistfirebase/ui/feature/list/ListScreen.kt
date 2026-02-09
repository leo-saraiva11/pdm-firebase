package com.example.todolistfirebase.ui.feature.list

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistfirebase.data.AppContainer
import com.example.todolistfirebase.navigation.AddEditRoute
import com.example.todolistfirebase.ui.components.TodoItem

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ListScreen(
    onNavigateToAddEdit: (String?) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel = viewModel<ListViewModel> {
        ListViewModel(
            todoRepository = AppContainer.todoRepository,
            authRepository = AppContainer.authRepository
        )
    }
    val todos by viewModel.todos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ListUiEvent.Navigate -> {
                    val route = event.route as? AddEditRoute
                    onNavigateToAddEdit(route?.id)
                }
                is ListUiEvent.ShowSnackbar -> {
                    // Implement snackbar if needed
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Tarefas") },
                actions = {
                    IconButton(onClick = {
                        viewModel.onLogoutClick()
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { paddingValues ->
        if (todos.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma tarefa ainda.\nToque no + para adicionar!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                items(todos, key = { it.id }) { todo ->
                    TodoItem(
                        modifier = Modifier.animateItemPlacement(),
                        todo = todo,
                        onCompletedChange = { isCompleted ->
                            viewModel.onCompletedChange(todo, isCompleted)
                        },
                        onItemClick = {
                            viewModel.onTodoClick(todo)
                        },
                        onDeleteClick = {
                            viewModel.onDeleteClick(todo.id)
                        }
                    )
                }
            }
        }
    }
}
