package hu.bme.nandiii.gamebrowser.domain.game.deals

data class DealsItem(
    val dealID: String,
    val dealRating: String,
    val gameID: String,
    val internalName: String,
    val isOnSale: String,
    val lastChange: Int,
    val metacriticLink: String?,
    val metacriticScore: String?,
    val normalPrice: String,
    val releaseDate: Int,
    val salePrice: String,
    val savings: String,
    val steamAppID: String,
    val steamRatingCount: String,
    val steamRatingPercent: String,
    val steamRatingText: String?,
    val storeID: String,
    val thumb: String,
    val title: String,
)