package hu.bme.nandiii.gamebrowser.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen(route = "login_screen")
    object GameListScreen : Screen(route = "game_list_screen")
    object GameDetailScreen : Screen(route = "games/{${Args.gameId}}") {
        fun passGameId(gameId: String) = "games/${gameId}"

        object Args {
            const val gameId = "68"
        }
    }

}