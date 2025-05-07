package hu.bme.nandiii.gamebrowser.domain.game.single

data class SingleGameQuery(
    val cheapestPriceEver: CheapestPriceEver,
    val deals: List<Deal>,
    val info: Info,
    var gameId: String = "",
)