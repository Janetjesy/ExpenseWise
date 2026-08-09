package com.janet.expensewise.expense.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.janet.expensewise.expense.data.Expense
import androidx.compose.foundation.clickable

@Composable
fun ExpenseCard(
    expense: Expense,
    onDelete: (Expense) -> Unit,
    onClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = expense.category, style = MaterialTheme.typography.titleMedium)
                if (!expense.note.isNullOrBlank()) {
                    Text(text = expense.note, style = MaterialTheme.typography.bodySmall)
                }
                Text(text = expense.date, style = MaterialTheme.typography.bodySmall)
            }

            Row {
                Text(
                    text = "KES ${expense.amount}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete expense")
                }
            }
        }
    }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                onDelete(expense)
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}