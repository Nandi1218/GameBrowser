package hu.bme.nandiii.gamebrowser.domain.game.favourites

data class Info(
    val gameID: String = "",
    val steamAppID: String? = "",
    val thumb: String = "",
    val title: String = "",
)