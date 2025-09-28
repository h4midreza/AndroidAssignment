package ir.miare.androidcodechallenge.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.miare.androidcodechallenge.presentation.followed.FollowedPlayersScreen
import ir.miare.androidcodechallenge.presentation.players.PlayersScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "players"
    ) {
        composable("players") {
            PlayersScreen(
                onNavigateToFollowed = {
                    navController.navigate("followed")
                }
            )
        }

        composable("followed") {
            FollowedPlayersScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
