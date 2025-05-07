package hu.bme.nandiii.gamebrowser.feature.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.nandiii.gamebrowser.data.auth.AuthService
import hu.bme.nandiii.gamebrowser.data.network.repository.FireStoreGameRepository
import hu.bme.nandiii.gamebrowser.data.network.retrofit.RetrofitGameRepository
import hu.bme.nandiii.gamebrowser.domain.game.deals.DealsItem
import hu.bme.nandiii.gamebrowser.domain.game.favourites.FavouriteGameItem
import hu.bme.nandiii.gamebrowser.domain.game.search.SearchedGamesItem
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.GamesState
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.ListState
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortDirection
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortState
import hu.bme.nandiii.gamebrowser.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val authService: AuthService,
    private val gameRepository: RetrofitGameRepository,
    private val fireStoreGameRepository: FireStoreGameRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(GamesState())
    val state = _state.asStateFlow()

    private val _gameListState = MutableStateFlow<GameListState>(GameListState.Loading)
    val gameListState = _gameListState.asStateFlow()


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val searchText get() = state.value.searchText
    private val dropdownExpanded get() = state.value.dropdownExpanded
    private val searchHistory get() = state.value.searchHistory

    init {
        getDeals()
    }

    private fun getSearchedGames() {
        if (searchText.trim().length < 3 || searchText.isBlank()) {
            _gameListState.value = GameListState.Empty
            return
        }
        viewModelScope.launch {
            _gameListState.value = GameListState.Loading
            try {
                gameRepository.getSearchedGames(searchText)
                    .enqueue(object : Callback<List<SearchedGamesItem>?> {
                        override fun onResponse(
                            call: Call<List<SearchedGamesItem>?>,
                            response: Response<List<SearchedGamesItem>?>,
                        ) {
                            if (response.isSuccessful) {
                                _gameListState.tryEmit(GameListState.SearchedList(response.body()))

                            }
                        }

                        override fun onFailure(call: Call<List<SearchedGamesItem>?>, t: Throwable) {
                            t.printStackTrace()
                            _gameListState.value = GameListState.Error(t)
                        }

                    })
            } catch (e: Exception) {
                _gameListState.value = GameListState.Error(e)

            }
        }
    }

    private fun getDeals() {
        viewModelScope.launch {
            _gameListState.value = GameListState.Loading
            try {
                gameRepository.getDeals(state.value.sortBy, state.value.sortDirection)
                    .enqueue(object : Callback<List<DealsItem>?> {
                        override fun onResponse(
                            call: Call<List<DealsItem>?>,
                            response: Response<List<DealsItem>?>,
                        ) {
                            if (response.isSuccessful) {
                                _gameListState.tryEmit(GameListState.DealsList(response.body()))
                            }
                        }

                        override fun onFailure(call: Call<List<DealsItem>?>, t: Throwable) {
                            t.printStackTrace()
                            _gameListState.value = GameListState.Error(t)
                        }

                    })
            } catch (e: Exception) {
                _gameListState.value = GameListState.Error(e)

            }
        }
    }

    private fun retrieveFavourites() {
        _gameListState.value = GameListState.Loading
        viewModelScope.launch {
            try {
                if (fireStoreGameRepository.getAllGames().isEmpty()) {
                    _gameListState.value = GameListState.Empty
                } else {
                    getFavourites(fireStoreGameRepository.getAllGames())
                }
            } catch (e: Exception) {
                _gameListState.value = GameListState.Error(e)
            }
        }
    }

    private fun getFavourites(ids: List<String>) {
        _gameListState.value = GameListState.Loading
        var idString = ""
        for (i in ids)
            idString += "$i,"
        idString = idString.dropLast(1)
        viewModelScope.launch {
            _gameListState.value = GameListState.Loading
            try {
                gameRepository.getMultipleGames(idString)
                    .enqueue(object : Callback<List<FavouriteGameItem>?> {
                        override fun onResponse(
                            call: Call<List<FavouriteGameItem>?>,
                            response: Response<List<FavouriteGameItem>?>,
                        ) {
                            if (response.isSuccessful) {
                                _gameListState.tryEmit(GameListState.FavouritesList(response.body()))

                            }
                        }

                        override fun onFailure(call: Call<List<FavouriteGameItem>?>, t: Throwable) {
                            t.printStackTrace()
                            _gameListState.value = GameListState.Error(t)
                        }

                    })
            } catch (e: Exception) {
                _gameListState.value = GameListState.Error(e)

            }
        }


    }

    fun onEvent(event: GameListEvent) {
        when (event) {
            is GameListEvent.Deals -> {
                getDeals()
            }

            is GameListEvent.Favourites -> {
                retrieveFavourites()
            }

            is GameListEvent.Search -> {
                getSearchedGames()
            }

            is GameListEvent.ChangeSortState -> {


                val newSortState = event.sortState
                _state.update {
                    it.copy(sortBy = newSortState, sortDirection = SortDirection.ASC)
                }
                if(_gameListState.value is GameListState.DealsList) //Only deals can be sorted on the Api level
                {
                    getDeals()
                }

            }

            is GameListEvent.SortDirectionChange -> {
                val newSortDirection = event.sortDirection
                _state.update {
                    it.copy(sortDirection = newSortDirection)
                }
                if(_gameListState.value is GameListState.DealsList) //Only deals can be sorted on the Api level
                    getDeals()

            }

            is GameListEvent.ChangedDropdownListState -> {
                val newListState = event.listState
                _state.update {
                    it.copy(dropdownListState = newListState)
                }
            }

            is GameListEvent.OnFavouriteClick -> {
                _gameListState.value = GameListState.Loading
            }

            is GameListEvent.SearchTextChange -> {
                val newSearchText = event.searchText
                _state.update {
                    it.copy(searchText = newSearchText)
                }
            }

            is GameListEvent.SearchActiveChange -> {
                val newSearchActive = event.searchActive
                _state.update {
                    it.copy(searchActive = newSearchActive)
                }
            }

            is GameListEvent.DropdownExpandedState -> {
                val newDropdownExpanded = event.dropdownExpanded
                _state.update {
                    it.copy(dropdownExpanded = newDropdownExpanded)
                }
            }

            is GameListEvent.SearchHistoryAdd -> {
                val newSearchTerm = event.searchTerm
                _state.update {
                    val tempList = searchHistory.toMutableList()
                    tempList.add(0, newSearchTerm)
                    it.copy(searchHistory = tempList.toList())
                }
            }

            GameListEvent.DropdownExpandedStateSwitch -> {
                _state.update {
                    it.copy(dropdownExpanded = !dropdownExpanded)
                }

            }


            else -> {}
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authService.signOut()
        }
    }
}


sealed class GameListEvent {
    data class SortDirectionChange(val sortDirection: SortDirection) : GameListEvent()
    data class ChangedDropdownListState(val listState: ListState) : GameListEvent()
    data class ChangeSortState(val sortState: SortState) : GameListEvent()
    data class SearchTextChange(val searchText: String) : GameListEvent()
    data class SearchActiveChange(val searchActive: Boolean) : GameListEvent()
    data class DropdownExpandedState(val dropdownExpanded: Boolean) : GameListEvent()
    data class SearchHistoryAdd(val searchTerm: String) : GameListEvent()
    data class OnFavouriteClick(val gameID: String) : GameListEvent()
    data object DropdownExpandedStateSwitch : GameListEvent()
    data object Search : GameListEvent()
    data object Deals : GameListEvent()
    data object Favourites : GameListEvent()

}


sealed class GameListState {
    data object Loading : GameListState()
    data class Error(val error: Throwable) : GameListState()
    data class DealsList(val dealsData: List<DealsItem>?) : GameListState()
    data class SearchedList(val searchedData: List<SearchedGamesItem>?) : GameListState()
    data class FavouritesList(val favouritesData: List<FavouriteGameItem>?) : GameListState()
    data object Empty : GameListState()
}