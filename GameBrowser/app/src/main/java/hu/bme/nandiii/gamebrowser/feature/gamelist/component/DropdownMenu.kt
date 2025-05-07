package hu.bme.nandiii.gamebrowser.feature.gamelist.component

import android.graphics.Color.rgb
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import hu.bme.nandiii.gamebrowser.R

import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListEvent
import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuCustom(
    viewModel: GameListViewModel,
    state: State<GamesState>,
) {
    var dropDownState = state.value.dropdownListState
    ExposedDropdownMenuBox(expanded = state.value.dropdownExpanded, onExpandedChange = {
        viewModel.onEvent(
            GameListEvent.DropdownExpandedStateSwitch
        )
    }) {

        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier
            .menuAnchor()
            .clickable { viewModel.onEvent(GameListEvent.DropdownExpandedState(true)) }
            .padding(end = 8.dp)
            .size(36.dp))
        ExposedDropdownMenu(
            expanded = state.value.dropdownExpanded, onDismissRequest = {
                viewModel.onEvent(
                    GameListEvent.DropdownExpandedState(false)
                )
            }, modifier = Modifier
                .width(38.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .animateContentSize()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.discount),
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        if (state.value.dropdownListState != ListState.ONSALE) {
                            viewModel.onEvent(
                                GameListEvent.ChangedDropdownListState(
                                    listState = ListState.ONSALE
                                )
                            )
                            viewModel.onEvent(GameListEvent.DropdownExpandedState(false))
                            viewModel.onEvent(GameListEvent.Deals)
                        }
                    }
                    .size(36.dp),
                tint = if (dropDownState == ListState.ONSALE) Color(
                    rgb(
                        255,
                        215,
                        0
                    )
                ) else MaterialTheme.colorScheme.onPrimaryContainer
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        if (state.value.dropdownListState != ListState.FAVOURITE) {
                            viewModel.onEvent(
                                GameListEvent.ChangedDropdownListState(
                                    listState = ListState.FAVOURITE
                                )
                            )
                            viewModel.onEvent(GameListEvent.DropdownExpandedState(false))
                            viewModel.onEvent(GameListEvent.Favourites)
                        }
                    }
                    .size(36.dp),
                tint = if (dropDownState == ListState.FAVOURITE) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer

            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .clickable {
                        if (state.value.dropdownListState != ListState.SEARCHRESULTS) {
                            viewModel.onEvent(
                                GameListEvent.ChangedDropdownListState(
                                    listState = ListState.SEARCHRESULTS
                                )
                            )
                            viewModel.onEvent(GameListEvent.DropdownExpandedState(false))
                            viewModel.onEvent(GameListEvent.Search)
                        }
                    }
                    .size(36.dp),
                tint = if (dropDownState == ListState.SEARCHRESULTS) Color(
                    rgb(
                        192,
                        192,
                        192
                    )
                ) else MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

    }

}