package hu.bme.nandiii.gamebrowser.feature.gamelist

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.domain.game.favourites.FavouriteGameItem
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.DropdownMenuCustom
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.GamesState
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.ListState
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SearchBarCustom
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortDirection
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortState
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.lists.DealList
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.lists.FavList
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.lists.SearchList
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.sorting.SortingBar
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.state.EmptyScreen
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.state.ErrorScreen
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.state.LoadingScreen
import hu.bme.nandiii.gamebrowser.ui.common.SnackbarCommon
import hu.bme.nandiii.gamebrowser.util.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GameListScreen(
    viewModel: GameListViewModel = hiltViewModel(),
    onSignOut: () -> Unit = {},
    onGameItemClick: (String) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val state: State<GamesState> = viewModel.state.collectAsStateWithLifecycle()
    val gameListState: GameListState = viewModel.gameListState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = null)


    LaunchedEffect(uiEvent) {
        when (uiEvent) {

            is UiEvent.Success -> {

            }

            is UiEvent.Failure -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = (uiEvent as UiEvent.Failure).message.asString(context)
                    )
                }
            }

            null -> {}
        }

    }

    Scaffold(snackbarHost = {
        SnackbarCommon(snackbarHostState = snackbarHostState)
    }, topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(R.string.title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        },
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
            actions = {
                IconButton(onClick = {
                    viewModel.signOut()
                    onSignOut()
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.log_out),
                        contentDescription = "Log out",
                        modifier = Modifier.size(30.dp)
                    )
                }
                DropdownMenuCustom(viewModel = viewModel, state = state)
                Spacer(modifier = Modifier.width(8.dp))
            }

        )
    }) { contentPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding)
        ) {
            AnimatedVisibility(state.value.dropdownListState == ListState.SEARCHRESULTS) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBarCustom(viewModel = viewModel, state = state)
                }
            }

            SortingBar(viewModel)

            when (gameListState) {
                GameListState.Loading -> LoadingScreen()
                is GameListState.Error -> ErrorScreen(text = gameListState.error.message.toString())
                is GameListState.Empty -> EmptyScreen()

                is GameListState.SearchedList -> {
                    val games = gameListState.searchedData?.toList() ?: emptyList()
                    if (games.isNotEmpty()) {
                        if (state.value.sortDirection == SortDirection.ASC) {
                            SearchList(
                                games = games.sortedBy { it.external },
                                onGameItemClick = onGameItemClick
                            )


                            Log.d("NandiDebug", "First item: ${games.first().external}")
                        } else {
                            SearchList(
                                games = games.sortedBy { it.external }.reversed(),
                                onGameItemClick = onGameItemClick
                            )
                        }
                    } else EmptyScreen()

                }

                is GameListState.DealsList -> {
                    val deals = gameListState.dealsData?.toList() ?: emptyList()
                    if (deals.isNotEmpty()) {
                        when (state.value.sortBy) {
                            SortState.NAME -> if (state.value.sortDirection == SortDirection.ASC) DealList(
                                deals = deals.sortedBy { it.title },
                                onGameItemClick
                            ) else DealList(
                                deals = deals.sortedBy { it.title }.reversed(),
                                onGameItemClick
                            )

                            SortState.PRICE -> if (state.value.sortDirection == SortDirection.ASC) DealList(
                                deals = deals.sortedBy { it.salePrice },
                                onGameItemClick
                            ) else DealList(
                                deals = deals.sortedBy { it.salePrice }.reversed(),
                                onGameItemClick
                            )

                            SortState.RATING -> if (state.value.sortDirection == SortDirection.ASC) DealList(
                                deals = deals.sortedBy { 100 - it.salePrice.toDouble() / it.normalPrice.toDouble() },
                                onGameItemClick
                            ) else DealList(
                                deals = deals.sortedByDescending { 100 - it.salePrice.toDouble() / it.normalPrice.toDouble() },
                                onGameItemClick
                            )

                            else -> {
                                DealList(deals = deals, onGameItemClick)
                            }

                        }
                    } else EmptyScreen()

                }

                is GameListState.FavouritesList -> {
                    val favs: List<FavouriteGameItem> =
                        gameListState.favouritesData?.toList() ?: emptyList()
                    if (favs.isNotEmpty()) {
                        when(state.value.sortDirection)
                        {
                            SortDirection.ASC -> FavList(favs = favs.sortedBy { it.info.title }, onGameItemClick)
                            SortDirection.DESC -> FavList(favs = favs.sortedByDescending { it.info.title }, onGameItemClick)
                        }
                    } else EmptyScreen()
                }
            }
        }
    }
}
