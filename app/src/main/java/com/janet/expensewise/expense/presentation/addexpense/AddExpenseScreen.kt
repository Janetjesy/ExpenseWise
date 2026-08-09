package com.janet.expensewise.expense.presentation.addexpense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.janet.expensewise.expense.data.Expense
import com.janet.expensewise.expense.presentation.ExpenseViewModel

private val categories = listOf(
    "Food", "Transport", "Shopping", "Bills",
    "Entertainment", "Health", "Education", "Other"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel = viewModel(),
    existingExpense: Expense? = null,
    onExpenseSaved: () -> Unit
) {
    var amountText by remember { mutableStateOf(existingExpense?.amount?.toString() ?: "") }
    var selectedCategory by remember { mutableStateOf(existingExpense?.category) }
    var note by remember { mutableStateOf(existingExpense?.note ?: "") }
    var date by remember { mutableStateOf(existingExpense?.date ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isEditMode = existingExpense != null

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (isEditMode) "Edit Expense" else "Add Expense") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Category")
            Spacer(modifier = Modifier.height(4.dp))
            FlowRowCategories(
                categories = categories,
                selected = selectedCategory,
                onSelect = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (e.g. 2026-08-09)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()

                    errorMessage = when {
                        amountText.isBlank() -> "Amount cannot be empty"
                        amount == null -> "Amount must be a valid number"
                        amount <= 0 -> "Amount must be greater than zero"
                        selectedCategory == null -> "Please select a category"
                        date.isBlank() -> "Please enter a date"
                        else -> null
                    }

                    if (errorMessage == null) {
                        if (isEditMode) {
                            val updatedExpense = existingExpense!!.copy(
                                amount = amount!!,
                                category = selectedCategory!!,
                                note = note.ifBlank { null },
                                date = date
                            )
                            viewModel.updateExpense(updatedExpense)
                        } else {
                            viewModel.addExpense(
                                Expense(
                                    amount = amount!!,
                                    category = selectedCategory!!,
                                    note = note.ifBlank { null },
                                    date = date
                                )
                            )
                        }
                        onExpenseSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Update" else "Save")
            }
        }
    }
}

@Composable
private fun FlowRowCategories(
    categories: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column {
        categories.chunked(4).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { category ->
                    FilterChip(
                        selected = category == selected,
                        onClick = { onSelect(category) },
                        label = { Text(category) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}