package hu.bme.nandiii.gamebrowser.feature.gamelist.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListEvent
import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCustom(
    viewModel: GameListViewModel,
    state: State<GamesState>,
) {
    SearchBar(
        query = state.value.searchText,
        onQueryChange = { viewModel.onEvent(GameListEvent.SearchTextChange(it)) },
        onSearch = {
            if (state.value.searchHistory.contains(state.value.searchText)
                    .not() && state.value.searchText.isNotBlank() && state.value.searchText.length > 2
            ) {
                viewModel.onEvent(GameListEvent.SearchHistoryAdd(state.value.searchText))
                viewModel.onEvent(GameListEvent.Search)
                viewModel.onEvent(GameListEvent.SearchActiveChange(false))
            }
        },
        active = state.value.searchActive,
        onActiveChange = { viewModel.onEvent(GameListEvent.SearchActiveChange(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = state.value.searchActive) {
                IconButton(onClick = {
                    if (state.value.searchText.isNotEmpty()) viewModel.onEvent(
                        GameListEvent.SearchTextChange(
                            ""
                        )
                    )
                    else viewModel.onEvent(GameListEvent.SearchActiveChange(false))
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon"
                    )
                }
            }

        },
        content = {
            for (search in state.value.searchHistory)
                Surface(
                    onClick = {
                        viewModel.onEvent(GameListEvent.SearchTextChange(search))
                        viewModel.onEvent(GameListEvent.Search)
                        viewModel.onEvent(GameListEvent.SearchActiveChange(false))

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_history_24),
                            contentDescription = "History",
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Text(
                            text = search,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } },
        tonalElevation = 10.dp,
    )
}