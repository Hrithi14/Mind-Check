package com.mindcheck.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindcheck.app.presentation.dashboard.DashboardScreen
import com.mindcheck.app.presentation.dashboard.DashboardViewModel
import com.mindcheck.app.presentation.focus.FocusSelectionScreen
import com.mindcheck.app.presentation.welcome.WelcomeScreen

@Composable
fun MindCheckNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    dashboardViewModel: DashboardViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(Screen.FocusSelection.route)
                }
            )
        }

        composable(Screen.FocusSelection.route) {
            FocusSelectionScreen(
                onContinueClick = { selectedAreas ->
                    dashboardViewModel.setFocusAreas(selectedAreas)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onAdjustFocusClick = {
                    navController.navigate(Screen.FocusSelection.route)
                },
                viewModel = dashboardViewModel
            )
        }
    }
}
