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
import androidx.compose.material.icons.filled.CalendarToday
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import com.janet.expensewise.ui.theme.CategoryColors

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
            var showDatePicker by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = date,
                onValueChange = { },
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Pick date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            if (showDatePicker) {
                val initialMillis = date.toLongDateMillisOrNull()
                    ?: System.currentTimeMillis()

                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = initialMillis
                )

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                date = Instant.ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()
                                    .toString()
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

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

@OptIn(ExperimentalMaterial3Api::class)
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
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CategoryColors[category]?.copy(alpha = 0.25f)
                                ?: MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = CategoryColors[category]
                                ?: MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

//for the date
private fun String.toLongDateMillisOrNull(): Long? {
    return try {
        LocalDate.parse(this)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    } catch (e: Exception) {
        null
    }
}