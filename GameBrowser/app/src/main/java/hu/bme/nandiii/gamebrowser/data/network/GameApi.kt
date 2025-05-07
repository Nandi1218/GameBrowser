package hu.bme.nandiii.gamebrowser.data.network

import hu.bme.nandiii.gamebrowser.domain.game.deals.DealsItem
import hu.bme.nandiii.gamebrowser.domain.game.favourites.FavouriteGameItem
import hu.bme.nandiii.gamebrowser.domain.game.search.SearchedGamesItem
import hu.bme.nandiii.gamebrowser.domain.game.single.SingleGameQuery
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApi {
    @GET("/api/1.0/deals")
    fun getDeals(
        @Query("storeID") storeID: Int = 1,
        @Query("lowerPrice") lowerPrice: Int = 0,
        @Query("upperPrice") upperPrice: Int = 200,
        @Query("sortBy") sortBy: String = "DealRating",
        @Query("desc") desc: String = "0",
        @Query("onSale") onSale:String = "1",
    ): Call<List<DealsItem>?>

    @GET("/api/1.0/games")
    fun getGame(
        @Query("id") id: String,
    ): Call<SingleGameQuery?>

    @GET("/api/1.0/games")
    fun getSearchedGames(
        @Query("title") title: String,
    ): Call<List<SearchedGamesItem>?>

    @GET("/api/1.0/games")
    fun getMultipleGames(
        @Query("ids") ids: String,
        @Query("format") format: String = "array",
    ): Call<List<FavouriteGameItem>?>
}