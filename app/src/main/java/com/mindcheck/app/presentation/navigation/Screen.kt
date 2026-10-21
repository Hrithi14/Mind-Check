package com.mindcheck.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object FocusSelection : Screen("focus_selection")
    data object Dashboard : Screen("dashboard")
}
