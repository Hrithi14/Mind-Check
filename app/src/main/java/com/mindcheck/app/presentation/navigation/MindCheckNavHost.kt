package com.mindcheck.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindcheck.app.MindCheckApplication
import com.mindcheck.app.presentation.dashboard.DashboardScreen
import com.mindcheck.app.presentation.dashboard.DashboardViewModel
import com.mindcheck.app.presentation.focus.FocusSelectionScreen
import com.mindcheck.app.presentation.focus.FocusSelectionViewModel
import com.mindcheck.app.presentation.welcome.WelcomeScreen
import com.mindcheck.app.presentation.welcome.WelcomeViewModel

@Composable
fun MindCheckNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as? MindCheckApplication
    val container = app?.container

    val welcomeViewModel: WelcomeViewModel = viewModel {
        WelcomeViewModel(container?.userPreferencesRepository)
    }

    val focusViewModel: FocusSelectionViewModel = viewModel {
        FocusSelectionViewModel(container?.userPreferencesRepository)
    }

    val dashboardViewModel: DashboardViewModel = viewModel {
        DashboardViewModel(
            dailyCheckInRepository = container?.dailyCheckInRepository,
            preferencesRepository = container?.userPreferencesRepository
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    welcomeViewModel.completeWelcome()
                    navController.navigate(Screen.FocusSelection.route)
                },
                viewModel = welcomeViewModel
            )
        }

        composable(Screen.FocusSelection.route) {
            FocusSelectionScreen(
                onContinueClick = { selectedAreas ->
                    focusViewModel.saveSelection()
                    dashboardViewModel.setFocusAreas(selectedAreas)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = focusViewModel
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
