package hu.bme.nandiii.gamebrowser.feature.gamedatail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.nandiii.gamebrowser.data.network.repository.FireStoreGameRepository
import hu.bme.nandiii.gamebrowser.data.network.retrofit.IGameRepository
import hu.bme.nandiii.gamebrowser.domain.game.single.SingleGameQuery
import hu.bme.nandiii.gamebrowser.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(
    private val gameRepository: IGameRepository,
    private val fireStoreGameRepository: FireStoreGameRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var _state = MutableStateFlow<GameDetailState>(GameDetailState.Loading)
    val state = _state.asStateFlow()

    private var _gameId = MutableStateFlow<String>("")
    val gameId = _gameId.asStateFlow()

    private var _isFavourite = MutableStateFlow<Boolean>(false)
    val isFavourite = _isFavourite.asStateFlow()

    init {
        getGame(checkNotNull(savedStateHandle[Screen.GameDetailScreen.Args.gameId]))
        _gameId.value = checkNotNull(savedStateHandle[Screen.GameDetailScreen.Args.gameId])
        viewModelScope.launch {
            _isFavourite.value = fireStoreGameRepository.getGame(gameId.value) != null
        }
    }

    fun onEvent(event: GameDetailEvent) {
        when (event) {
            GameDetailEvent.OnFavouriteClick -> {
                _isFavourite.value = !_isFavourite.value
                Log.d("NandiDebug", "isFavourite: ${_isFavourite.value}")
                viewModelScope.launch {
                    if (_isFavourite.value) {
                        if (fireStoreGameRepository.getGame(gameId.value) == null) {
                            Log.d("NandiDebug", "Adding game to firestore")
                            fireStoreGameRepository.saveGame(gameId.value)
                        }
                    } else {
                        Log.d("NandiDebug", "Deleting game from firestore")
                        fireStoreGameRepository.deleteGame(gameID = gameId.value)
                    }
                }
            }
        }
    }

    private fun getGame(gameId: String) {
        viewModelScope.launch {
            _state.value = GameDetailState.Loading
            try {
                gameRepository.getGame(gameId).enqueue(object : Callback<SingleGameQuery?> {
                    override fun onResponse(
                        call: Call<SingleGameQuery?>,
                        response: Response<SingleGameQuery?>,
                    ) {
                        if (response.isSuccessful) {
                            _state.tryEmit(GameDetailState.Success(response.body()))
                        }
                    }

                    override fun onFailure(call: Call<SingleGameQuery?>, t: Throwable) {
                        Log.d("NANDI", "getGame onFailure ${t.message}")
                        t.printStackTrace()
                        _state.value = GameDetailState.Error(t)
                    }
                })
            } catch (e: Exception) {
                Log.d("NANDI", "getGame Exception ${e.message}")
                _state.value = GameDetailState.Error(e)
            }
        }
    }

}

sealed class GameDetailEvent {
    data object OnFavouriteClick : GameDetailEvent()
}

sealed class GameDetailState {
    data object Loading : GameDetailState()
    data class Error(val error: Throwable) : GameDetailState()
    data class Success(val dealsData: SingleGameQuery?) : GameDetailState()
}