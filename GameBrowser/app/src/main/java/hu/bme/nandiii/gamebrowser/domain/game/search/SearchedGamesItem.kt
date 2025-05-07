package hu.bme.nandiii.gamebrowser.domain.game.search

data class SearchedGamesItem(
    val cheapest: String,
    val cheapestDealID: String,
    val external: String,
    val gameID: String,
    val internalName: String,
    val steamAppID: String?,
    val thumb: String,
)
