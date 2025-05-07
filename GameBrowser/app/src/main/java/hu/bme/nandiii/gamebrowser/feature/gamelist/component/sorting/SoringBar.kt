package hu.bme.nandiii.gamebrowser.feature.gamelist.component.sorting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListViewModel
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.GamesState
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SetSorting
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortDirection
import hu.bme.nandiii.gamebrowser.feature.gamelist.component.SortState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.nandiii.gamebrowser.feature.gamelist.GameListState


@Composable
fun SortingBar(viewModel: GameListViewModel){
    val state: State<GamesState> = viewModel.state.collectAsStateWithLifecycle()
    val gameListState: GameListState = viewModel.gameListState.collectAsStateWithLifecycle().value
    Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(30.dp)
                    .weight(1F).let{
                        if(gameListState is GameListState.DealsList)
                    it.clickable { SetSorting(viewModel, SortState.RATING) }
                        else it
                    })
            {
                Text(
                    text = "Discount",
                    style = if(gameListState is GameListState.DealsList)  MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(start = 10.dp)
                )
                AnimatedVisibility(visible = (state.value.sortBy == SortState.RATING)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = if (state.value.sortDirection == SortDirection.ASC) R.drawable.arrow_up else R.drawable.arrow_down),
                        contentDescription = "sort"
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VerticalDivider(thickness = 1.dp, modifier = Modifier.height(15.dp))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(30.dp)
                    .weight(1F).let{
                        if(gameListState is GameListState.DealsList)
                    it.clickable { SetSorting(viewModel, SortState.PRICE) }
                        else it
                    }) {
                Text(
                    text = "Price",
                    style = if(gameListState is GameListState.DealsList)  MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(start = 10.dp)
                )
                AnimatedVisibility(visible = state.value.sortBy == SortState.PRICE) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = if (state.value.sortDirection == SortDirection.ASC) R.drawable.arrow_up else R.drawable.arrow_down),
                        contentDescription = "sort"
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VerticalDivider(thickness = 1.dp, modifier = Modifier.height(15.dp))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(30.dp)
                    .weight(1F)
                    .clickable { SetSorting(viewModel, SortState.NAME) })
            {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 10.dp)
                )
                AnimatedVisibility(visible = state.value.sortBy == SortState.NAME) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = if (state.value.sortDirection == SortDirection.ASC) R.drawable.arrow_up else R.drawable.arrow_down),
                        contentDescription = "sort"
                    )
                }
            }
        }
    }
}