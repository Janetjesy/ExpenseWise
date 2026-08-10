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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel = viewModel()
) {
    val allExpenses by viewModel.allExpenses.collectAsState()
    
    val total = allExpenses.sumOf { it.amount }

    val categoryTotals = allExpenses
        .groupBy { it.category }
        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
        .toList()
        .sortedByDescending { (_, amount) -> amount }

    val highestCategory = categoryTotals.firstOrNull()

    val pieData = categoryTotals.mapIndexed { index, (category, amount) ->
        Pie(
            label = category,
            data = amount,
            color = categoryColors[index % categoryColors.size],
            selectedColor = categoryColors[index % categoryColors.size]
        )
    }

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
                text = "Total Expenses: KES $total",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (highestCategory != null) {
                Text(
                    text = "Main Spending: ${highestCategory.first} (KES ${highestCategory.second})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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
                Text("No expenses recorded.")
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

private val categoryColors = listOf(
    Color(0xFF8DB355),
    Color(0xFFEF9A9A),
    Color(0xFF90CAF9),
    Color(0xFFFFCC80),
    Color(0xFFCE93D8),
    Color(0xFFA5D6A7),
    Color(0xFFFFAB91),
    Color(0xFF80DEEA)
)