package hu.bme.nandiii.gamebrowser.data.network.retrofit

import com.squareup.moshi.Moshi
import hu.bme.nandiii.gamebrowser.data.network.GameApi
import hu.bme.nandiii.gamebrowser.domain.game.deals.DealsItem
import hu.bme.nandiii.gamebrowser.domain.game.favourites.FavouriteGameItem
import hu.bme.nandiii.gamebrowser.domain.game.search.SearchedGamesItem
import hu.bme.nandiii.gamebrowser.domain.game.single.SingleGameQuery
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortDirection
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortState
import retrofit2.Call
import retrofit2.Retrofit
import javax.inject.Inject

class RetrofitGameRepository @Inject constructor(
    private val moshi: Moshi,
    private val retrofit: Retrofit,
    private val gameApi: GameApi,
) : IGameRepository {
    override suspend fun getGame(gameId: String): Call<SingleGameQuery?> {
        return gameApi.getGame(gameId)
    }

    override suspend fun getSearchedGames(searchTerm: String): Call<List<SearchedGamesItem>?> {
        return gameApi.getSearchedGames(searchTerm)
    }

    override suspend fun getDeals(sortBy: SortState, dir:SortDirection): Call<List<DealsItem>?> {
        var dirString = if(dir == SortDirection.DESC) "1" else "0"
        return when (sortBy) {
            SortState.NAME -> gameApi.getDeals(sortBy = "Title",desc =dirString)
            SortState.PRICE -> gameApi.getDeals(sortBy = "Price",desc =dirString)
            SortState.RATING -> gameApi.getDeals(sortBy = "Savings",desc =if(dir == SortDirection.DESC) "0" else "1")
            else -> gameApi.getDeals()
        }
    }

    override suspend fun getMultipleGames(gameIds: String): Call<List<FavouriteGameItem>?> {
        return gameApi.getMultipleGames(gameIds)
    }

}