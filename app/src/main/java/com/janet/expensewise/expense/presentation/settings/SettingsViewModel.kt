package com.janet.expensewise.expense.presentation.settings



import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("expensewise_prefs", Context.MODE_PRIVATE)

    private var _isDarkMode by mutableStateOf(prefs.getBoolean("dark_mode", false))
    val isDarkMode: Boolean get() = _isDarkMode

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode = enabled
        prefs.edit().putBoolean("dark_mode", enabled).apply()
    }
}