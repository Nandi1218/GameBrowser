package hu.bme.nandiii.gamebrowser.domain.game.favourites

data class FavouriteGameItem(
    val cheapestPriceEver: CheapestPriceEver = CheapestPriceEver(),
    val deals: List<Deal> = listOf(),
    val info: Info = Info(),
)