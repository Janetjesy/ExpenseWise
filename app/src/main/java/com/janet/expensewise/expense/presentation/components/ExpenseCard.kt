package com.janet.expensewise.expense.presentation.components



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.janet.expensewise.expense.data.Expense

@Composable
fun ExpenseCard(expense: Expense) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
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
            Text(
                text = "KES ${expense.amount}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}