package com.janet.expensewise.expense.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.janet.expensewise.expense.presentation.ExpenseViewModel
import com.janet.expensewise.expense.presentation.components.ExpenseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ExpenseViewModel = viewModel()
) {
    val expenses by viewModel.allExpenses.collectAsState()

    val totalThisMonth = expenses.sumOf { it.amount }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ExpenseWise") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: navigate to Add screen */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Total this month: KES $totalThisMonth",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (expenses.isEmpty()) {
                Text("No expenses yet. Tap + to add one.")
            } else {
                LazyColumn {
                    items(expenses) { expense ->
                        ExpenseCard(expense = expense)
                    }
                }
            }
        }
    }
}