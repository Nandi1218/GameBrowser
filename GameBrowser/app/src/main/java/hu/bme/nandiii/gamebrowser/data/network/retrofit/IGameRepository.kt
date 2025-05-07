package hu.bme.nandiii.gamebrowser.data.network.retrofit

import hu.bme.nandiii.gamebrowser.domain.game.deals.DealsItem
import hu.bme.nandiii.gamebrowser.domain.game.favourites.FavouriteGameItem
import hu.bme.nandiii.gamebrowser.domain.game.search.SearchedGamesItem
import hu.bme.nandiii.gamebrowser.domain.game.single.SingleGameQuery
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortDirection
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortState
import retrofit2.Call

interface IGameRepository {
    suspend fun getGame(gameId: String): Call<SingleGameQuery?>
    suspend fun getSearchedGames(searchTerm: String): Call<List<SearchedGamesItem>?>
    suspend fun getDeals(sortBy: SortState, dir: SortDirection): Call<List<DealsItem>?>
    suspend fun getMultipleGames(gameIds: String): Call<List<FavouriteGameItem>?>

}