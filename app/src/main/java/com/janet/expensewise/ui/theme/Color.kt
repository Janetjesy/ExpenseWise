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

// Dark theme variants(for settings)
val DarkPrimary = Color(0xFFA0C97A)
val DarkOnPrimary = Color(0xFF16290A)
val DarkPrimaryContainer = Color(0xFF3A4F26)
val DarkOnPrimaryContainer = Color(0xFFDCEDC0)

val DarkSecondary = Color(0xFFB8C2A9)
val DarkOnSecondary = Color(0xFF272E1F)
val DarkSecondaryContainer = Color(0xFF3E4634)
val DarkOnSecondaryContainer = Color(0xFFE1E4D5)

val DarkTertiary = Color(0xFFE3A583)
val DarkOnTertiary = Color(0xFF442008)
val DarkTertiaryContainer = Color(0xFF5D3319)
val DarkOnTertiaryContainer = Color(0xFFFDDBC7)

val DarkBackground = Color(0xFF1B1C18)
val DarkOnBackground = Color(0xFFE4E3DA)

val DarkSurface = Color(0xFF242620)
val DarkOnSurface = Color(0xFFE4E3DA)
val DarkSurfaceVariant = Color(0xFF383A32)

val DarkOutline = Color(0xFF8F9285)