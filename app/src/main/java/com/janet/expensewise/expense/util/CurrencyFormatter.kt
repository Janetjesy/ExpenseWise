package com.janet.expensewise.expense.util



import java.text.NumberFormat
import java.util.Locale

private val amountFormat = NumberFormat.getNumberInstance(Locale.US).apply {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
}

fun formatCurrency(amount: Double): String {
    return "KES ${amountFormat.format(amount)}"
}