package hu.bme.nandiii.gamebrowser.feature.gamedatail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.domain.game.single.SingleGameQuery
import hu.bme.nandiii.gamebrowser.feature.gamedatail.component.DealCard
import java.time.Instant
import java.time.LocalDateTime
import androidx.compose.foundation.lazy.LazyColumn as LazyColumn1

@Composable
fun GameDetailScreen(
    viewModel: GameDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val toggleState = viewModel.isFavourite.collectAsStateWithLifecycle().value
    when (state) {
        is GameDetailState.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.surface,
                )
            }
        }

        is GameDetailState.Error -> {
            Text(text = state.error.message.toString())
        }

        is GameDetailState.Success -> {
            val game: SingleGameQuery = checkNotNull(state.dealsData)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 64.dp)
                        .defaultMinSize(400.dp), shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Card(
                                modifier = Modifier.padding(bottom = 16.dp),
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                border = BorderStroke(0.5.dp, Color.Yellow)
                            ){
                                Text(
                                    text = game.info.title,
                                    style = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                )
                            }

                        }
                        Card(
                            modifier = Modifier.padding(bottom = 16.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                            border = BorderStroke(0.5.dp, color = Color.Yellow)){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(game.info.thumb).crossfade(enable = true).build(),
                                contentDescription = game.info.title + " image",
                                placeholder = painterResource(R.drawable.placeholder),
                                modifier = Modifier
                                    .width(256.dp)
                                    .height(100.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            IconToggleButton(
                                checked = toggleState, onCheckedChange =
                                {
                                    viewModel.onEvent(GameDetailEvent.OnFavouriteClick)
                                },
                                modifier = Modifier.defaultMinSize(32.dp)
                            ) {
                                if (toggleState)
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_star_24),
                                        contentDescription = "favourited",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color.Yellow
                                    )
                                else
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.star_false),
                                        contentDescription = "not favourited",
                                        modifier = Modifier.size(32.dp)
                                    )
                            }
                        } }

                        Column {
                            Card {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        text = "SteamAppID:",
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(text = game.info.steamAppID.toString())
                                }
                                HorizontalDivider(modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(), thickness = 0.5.dp, color = Color.White)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(
                                        text = "Cheapest ever price:",
                                        modifier = Modifier.padding(end = 8.dp),
                                    )
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = LocalDateTime.ofInstant(Instant.ofEpochSecond(game.cheapestPriceEver.date.toLong()), java.util.TimeZone.getDefault().toZoneId()).toLocalDate().toString())
                                        Text(text = "$" + game.cheapestPriceEver.price,style = MaterialTheme.typography.bodyMedium.copy(color = Color.Green))
                                    }

                                }
                            }

                            Text(
                                text = "Current and prior deals:",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        LazyColumn1(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            items(items = game.deals) { item ->
                                DealCard(deal = item)
                            }
                        }
                    }
                }
            }

        }

    }
}

