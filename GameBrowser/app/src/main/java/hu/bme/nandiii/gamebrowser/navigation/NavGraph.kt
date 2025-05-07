package hu.bme.nandiii.gamebrowser.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.nandiii.gamebrowser.feature.auth.login.LoginScreen
import hu.bme.nandiii.gamebrowser.feature.gamedatail.GameDetailScreen
import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListScreen


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.LoginScreen.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    )
    {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(Screen.GameListScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(Screen.GameListScreen.route) {
            GameListScreen(onSignOut = {
                navController.navigate(Screen.LoginScreen.route)
            },
                onGameItemClick = {
                    navController.navigate(Screen.GameDetailScreen.passGameId(gameId = it))
                }
            )
        }
        composable(
            route = Screen.GameDetailScreen.route,
            arguments = listOf(navArgument(Screen.GameDetailScreen.Args.gameId) {
                defaultValue = "612"
                type = NavType.StringType
            }
            )
        ) {
            GameDetailScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}