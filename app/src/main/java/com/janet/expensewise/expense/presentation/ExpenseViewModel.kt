package com.janet.expensewise.expense.presentation


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.janet.expensewise.expense.data.Expense
import com.janet.expensewise.expense.data.ExpenseDatabase
import com.janet.expensewise.expense.data.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.YearMonth


class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository

    val allExpenses: StateFlow<List<Expense>>

    init {
        val dao = ExpenseDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(dao)

        allExpenses = repository.allExpenses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }
    fun getExpensesForMonth(expenses: List<Expense>, yearMonth: YearMonth): List<Expense> {
        return expenses.filter { expense ->
            try {
                val expenseMonth = YearMonth.parse(expense.date.substring(0, 7))
                expenseMonth == yearMonth
            } catch (e: Exception) {
                false
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    suspend fun getExpenseById(expenseId: Int): Expense? {
        return repository.getExpenseById(expenseId)
    }
}