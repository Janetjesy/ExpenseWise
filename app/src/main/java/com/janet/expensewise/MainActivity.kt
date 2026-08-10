package com.janet.expensewise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.janet.expensewise.expense.data.Expense
import com.janet.expensewise.expense.presentation.ExpenseViewModel
import com.janet.expensewise.expense.presentation.addexpense.AddExpenseScreen
import com.janet.expensewise.expense.presentation.details.ExpenseDetailsScreen
import com.janet.expensewise.expense.presentation.home.HomeScreen
import com.janet.expensewise.ui.theme.ExpenseWiseTheme
import com.janet.expensewise.expense.presentation.dashboard.DashboardScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseWiseTheme {
                ExpenseWiseApp()
            }
        }
    }
}

@Composable
fun ExpenseWiseApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onAddExpenseClick = {
                    navController.navigate("add_expense")
                },
                onExpenseClick = { expenseId ->
                    navController.navigate("expense_details/$expenseId")
                },
                onDashboardClick = {
                    navController.navigate("dashboard")
                }
            )
        }

        composable("dashboard") {
            DashboardScreen()
        }

        composable("add_expense") {
            AddExpenseScreen(
                onExpenseSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "expense_details/{expenseId}",
            arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: 0

            ExpenseDetailsScreen(
                expenseId = expenseId,
                onBack = { navController.popBackStack() },
                onEdit = { expense ->
                    navController.navigate("edit_expense/${expense.id}")
                },
                onDeleted = { navController.popBackStack() }
            )
        }

        composable(
            route = "edit_expense/{expenseId}",
            arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: 0
            var loadedExpense by remember { mutableStateOf<Expense?>(null) }
            val viewModel: ExpenseViewModel = viewModel()

            LaunchedEffect(expenseId) {
                loadedExpense = viewModel.getExpenseById(expenseId)
            }

            loadedExpense?.let { expenseToEdit ->
                AddExpenseScreen(
                    existingExpense = expenseToEdit,
                    onExpenseSaved = {
                        navController.popBackStack("home", inclusive = false)
                    }
                )
            }
        }
    }
}