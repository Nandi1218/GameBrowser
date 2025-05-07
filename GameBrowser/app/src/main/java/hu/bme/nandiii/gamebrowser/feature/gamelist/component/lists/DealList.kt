package hu.bme.nandiii.gamebrowser.feature.gamelist.component.lists

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.domain.game.deals.DealsItem
import kotlin.math.roundToInt

@Composable
fun DealList(deals: List<DealsItem>, onGameItemClick: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        items(deals) { item ->
            ElevatedCard(
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable(onClick = { onGameItemClick(item.gameID) })
                    .border(
                        0.5.dp,
                        if (false) MaterialTheme.colorScheme.primary else Color.Transparent
                    ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),


                ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.thumb).crossfade(enable = true).build(),
                        contentDescription = item.title + " image",
                        placeholder = painterResource(R.drawable.placeholder),
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp)
                            .padding(end = 4.dp)
                    )

                    if (item.storeID == "1")
                        Icon(
                            painter = painterResource(id = R.drawable.steam_logo),
                            contentDescription = "Steam logo",
                            modifier = Modifier.padding(4.dp)
                        )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = Modifier.padding(end = 4.dp),
                            text = "${100 - (item.salePrice.toDouble() / item.normalPrice.toDouble() * 100).roundToInt()}%",
                            style = TextStyle(color = Color.Green, fontSize = 24.sp)
                        )
                        Column {
                            Text(
                                text = item.salePrice,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = item.normalPrice,
                                style = TextStyle(textDecoration = TextDecoration.LineThrough)
                            )
                        }


                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                            Text(text = item.dealRating + "/10")

                        }
                    }
                }
            }
        }
    }
}