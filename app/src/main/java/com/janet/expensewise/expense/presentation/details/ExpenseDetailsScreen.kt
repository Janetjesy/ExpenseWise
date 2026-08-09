package com.janet.expensewise.expense.presentation.details



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.janet.expensewise.expense.data.Expense
import com.janet.expensewise.expense.presentation.ExpenseViewModel
import com.janet.expensewise.expense.presentation.components.ConfirmDeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailsScreen(
    expenseId: Int,
    viewModel: ExpenseViewModel = viewModel(),
    onBack: () -> Unit,
    onEdit: (Expense) -> Unit,
    onDeleted: () -> Unit
) {
    var expense by remember { mutableStateOf<Expense?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(expenseId) {
        expense = viewModel.getExpenseById(expenseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        val currentExpense = expense

        if (currentExpense == null) {
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Category: ${currentExpense.category}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Amount: KES ${currentExpense.amount}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Date: ${currentExpense.date}")
                Spacer(modifier = Modifier.height(8.dp))
                if (!currentExpense.note.isNullOrBlank()) {
                    Text("Note: ${currentExpense.note}")
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Button(onClick = { onEdit(currentExpense) }) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete")
                    }
                }
            }
        }
    }

    if (showDeleteDialog && expense != null) {
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.deleteExpense(expense!!)
                showDeleteDialog = false
                onDeleted()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}