# ExpenseWise

A simple, beginner-friendly personal expense tracker for Android, built with Kotlin and Jetpack Compose.

## Features

- Add, view, edit, and delete expenses
- Predefined expense categories (Food, Transport, Shopping, Bills, Entertainment, Health, Education, Other)
- Pick expense dates with a native Material 3 date picker
- Monthly total spending, category breakdown, and a pie chart on the Dashboard
- Color-coded categories throughout the app (expense list, category picker, pie chart)
- Confirmation dialog before deleting an expense

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Database:** Room
- **Architecture:** ViewModel + Repository (feature-based packaging, no DI framework)
- **Async:** Kotlin Coroutines & Flow
- **Navigation:** Navigation Compose
- **Charts:** [Compose Charts](https://github.com/ehsannarmani/ComposeCharts) by ehsannarmani

## Project Structure

```
com.janet.expensewise
└── expense
    ├── data
    │   ├── Expense.kt              # Room entity
    │   ├── ExpenseDao.kt           # Database queries
    │   ├── ExpenseDatabase.kt      # Room database singleton
    │   └── ExpenseRepository.kt    # Data access layer
    └── presentation
        ├── components/             # Reusable UI pieces
        ├── home/                   # Home screen (expense list)
        ├── addexpense/             # Add/Edit expense form
        ├── details/                # Expense details screen
        ├── dashboard/              # Monthly totals & charts
        └── ExpenseViewModel.kt
```

## Screens

| Screen | Description |
|---|---|
| Home | Shows total spending this month and a list of all expenses |
| Add/Edit Expense | Form to add a new expense or edit an existing one |
| Expense Details | View a single expense with Edit and Delete actions |
| Dashboard | Monthly total, highest spending category, category breakdown, and a pie chart |

## Getting Started

1. Clone the repo
2. Open in Android Studio (this project targets `compileSdk 37`, `minSdk 24`)
3. Let Gradle sync — dependencies are managed via the version catalog (`gradle/libs.versions.toml`)
4. Run on an emulator or physical device

## Status

This is a V1 learning project. All core CRUD functionality, navigation, and the dashboard are complete. Built incrementally, stage by stage, as a hands-on way to learn Android development with modern Jetpack Compose tooling.

## Roadmap Ideas

- Search/filter expenses
- Export data
- Custom categories
- Multi-month history view on the Dashboard