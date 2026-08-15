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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import com.janet.expensewise.expense.presentation.components.TotalSpendingCard
import com.janet.expensewise.ui.theme.CategoryColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.janet.expensewise.expense.util.formatCurrency
import androidx.compose.material.icons.filled.PieChartOutline
import com.janet.expensewise.expense.presentation.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel = viewModel(),
    onBack: () -> Unit
) {
    val allExpenses by viewModel.allExpenses.collectAsState()

    val total = allExpenses.sumOf { it.amount }

    val categoryTotals = allExpenses
        .groupBy { it.category }
        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
        .toList()
        .sortedByDescending { (_, amount) -> amount }

    val highestCategory = categoryTotals.firstOrNull()

    val pieData = categoryTotals.map { (category, amount) ->
        Pie(
            label = category,
            data = amount,
            color = CategoryColors[category] ?: Color.Gray,
            selectedColor = CategoryColors[category] ?: Color.Gray
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TotalSpendingCard(
                label = "Total this month",
                amount = total,
                subtitle = highestCategory?.let { "Highest spending: ${it.first} (${formatCurrency(it.second)})" }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (pieData.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    PieChart(
                        modifier = Modifier.size(220.dp),
                        data = pieData,
                        style = Pie.Style.Fill
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Category Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (categoryTotals.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.PieChartOutline,
                    message = "No expenses recorded yet."
                )
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
            Text(formatCurrency(amount))
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier.fillMaxWidth()
        )
    }
}