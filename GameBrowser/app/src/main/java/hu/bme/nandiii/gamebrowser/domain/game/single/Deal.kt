package hu.bme.nandiii.gamebrowser.domain.game.single

data class Deal(
    val dealID: String,
    val price: String,
    val retailPrice: String,
    val savings: String,
    val storeID: String,
)