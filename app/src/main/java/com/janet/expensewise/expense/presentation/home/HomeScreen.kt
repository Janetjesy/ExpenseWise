package com.janet.expensewise.expense.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.janet.expensewise.expense.presentation.ExpenseViewModel
import com.janet.expensewise.expense.presentation.components.ExpenseCard
import com.janet.expensewise.expense.presentation.components.TotalSpendingCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import com.janet.expensewise.expense.presentation.HomeTab
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.res.painterResource
import com.janet.expensewise.R
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.SearchOff
import com.janet.expensewise.expense.presentation.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ExpenseViewModel = viewModel(),
    onAddExpenseClick: () -> Unit,
    onExpenseClick: (Int) -> Unit,
    onDashboardClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val expenses by viewModel.allExpenses.collectAsState()
    var selectedTab by remember { mutableStateOf(HomeTab.TODAY) }
    val filteredExpenses = viewModel.getFilteredExpenses(expenses, selectedTab)
    val totalForTab = filteredExpenses.sumOf { it.amount }
//for searching feature
    var searchQuery by remember { mutableStateOf("") }

    val searchedExpenses = if (searchQuery.isBlank()) {
        filteredExpenses
    } else {
        filteredExpenses.filter { expense ->
            expense.category.contains(searchQuery, ignoreCase = true) ||
                    expense.note?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "ExpenseWise logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ExpenseWise")
                    }
                },
                actions = {
                    IconButton(onClick = onDashboardClick) {
                        Icon(Icons.Default.PieChart, contentDescription = "Dashboard")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpenseClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                HomeTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TotalSpendingCard(
                label = "Total (${selectedTab.label})",
                amount = totalForTab
            )

            Spacer(modifier = Modifier.height(16.dp))

            //for searching feature
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by category or note") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchedExpenses.isEmpty()) {
                EmptyState(
                    icon = if (searchQuery.isBlank()) Icons.Default.Inbox else Icons.Default.SearchOff,
                    message = if (searchQuery.isBlank()) "No expenses for this period."
                    else "No expenses match \"$searchQuery\"."
                )
            } else {
                LazyColumn {
                    items(searchedExpenses) { expense ->
                        ExpenseCard(
                            expense = expense,
                            onDelete = { viewModel.deleteExpense(it) },
                            onClick = { onExpenseClick(expense.id) }
                        )
                    }
                }
            }
        }
    }
}