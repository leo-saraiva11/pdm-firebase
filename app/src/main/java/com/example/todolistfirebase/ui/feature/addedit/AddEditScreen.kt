package com.example.todolistfirebase.ui.feature.addedit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistfirebase.data.AppContainer

/** Gemini - início
 * Prompt: Fix AddEditScreen to use state-based navigation
 */
@Composable
fun AddEditScreen(
    todoId: String?,
    onNavigateBack: () -> Unit
) {
    val viewModel = viewModel<AddEditViewModel> {
        AddEditViewModel(
            todoId = todoId,
            todoRepository = AppContainer.todoRepository,
            authRepository = AppContainer.authRepository
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }

    // Observe shouldNavigateBack state
    LaunchedEffect(viewModel.shouldNavigateBack) {
        if (viewModel.shouldNavigateBack) {
            onNavigateBack()
        }
    }
    
    // Observe snackbar messages
    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onSnackbarShown()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onSaveClick() }
            ) {
                if (viewModel.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Save")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isSaving
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Descrição (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                enabled = !viewModel.isSaving
            )
        }
    }
}
/** Gemini - final */


