package hu.bme.nandiii.gamebrowser.feature.gamelist.component
import androidx.compose.runtime.State
import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListEvent

import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListViewModel

fun SetSorting(viewModel: GameListViewModel, sortState: SortState)
{

    if (viewModel.state.value.sortBy == sortState) {
        if (viewModel.state.value.sortDirection == SortDirection.ASC)
            viewModel.onEvent(
                GameListEvent.SortDirectionChange(
                    SortDirection.DESC
                )
            )
        else
            viewModel.onEvent(
                GameListEvent.SortDirectionChange(
                    SortDirection.ASC
                )
            )
    } else
        viewModel.onEvent(GameListEvent.ChangeSortState(sortState))
}