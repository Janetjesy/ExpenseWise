package com.janet.expensewise.expense.presentation.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.janet.expensewise.expense.presentation.ExpenseViewModel
import java.time.YearMonth
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel = viewModel()
) {
    val allExpenses by viewModel.allExpenses.collectAsState()
    val currentMonth = YearMonth.now()
    val monthExpenses = viewModel.getExpensesForMonth(allExpenses, currentMonth)

    val total = monthExpenses.sumOf { it.amount }

    val categoryTotals = monthExpenses
        .groupBy { it.category }
        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
        .toList()
        .sortedByDescending { (_, amount) -> amount }

    val highestCategory = categoryTotals.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dashboard") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Total this month: KES $total",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (highestCategory != null) {
                Text(
                    text = "Highest spending: ${highestCategory.first} (KES ${highestCategory.second})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Category Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (categoryTotals.isEmpty()) {
                Text("No expenses recorded this month.")
            } else {
                LazyColumn {
                    items(categoryTotals) { (category, amount) ->
                        CategoryBar(
                            category = category,
                            amount = amount,
                            percentage = if (total > 0) (amount / total).toFloat() else 0f
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBar(category: String, amount: Double, percentage: Float) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(category)
            Text("KES $amount")
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier.fillMaxWidth()
        )
    }
}