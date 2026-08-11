package com.janet.expensewise.ui.theme

import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF8DB355)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFDCEDC0)
val OnPrimaryContainer = Color(0xFF26350B)

val Secondary = Color(0xFF5C6B4F)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFE1E4D5)
val OnSecondaryContainer = Color(0xFF2E3527)

val Tertiary = Color(0xFFC77B58)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFFDDBC7)
val OnTertiaryContainer = Color(0xFF3D1F0F)

val Background = Color(0xFFFAF9F5)
val OnBackground = Color(0xFF1B1C18)

val Surface = Color(0xFFFFFFFF)
val OnSurface = Color(0xFF1B1C18)
val SurfaceVariant = Color(0xFFF0EFE6)

val Outline = Color(0xFFC5C8BA)

val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)

// Shared per-category palette — used on the Dashboard pie chart,
// expense cards, and category chips, so each category has one
// consistent color everywhere in the app.

val CategoryColors = mapOf(
    "Food" to Color(0xFF8DB355),
    "Transport" to Color(0xFF5B8AA6),
    "Shopping" to Color(0xFFC77B58),
    "Bills" to Color(0xFF9575CD),
    "Entertainment" to Color(0xFFE0A458),
    "Health" to Color(0xFFE06B6B),
    "Education" to Color(0xFF4FA88C),
    "Other" to Color(0xFF9E9E8A)
)