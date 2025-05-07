package hu.bme.nandiii.gamebrowser.feature.gamedatail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hu.bme.nandiii.gamebrowser.R
import hu.bme.nandiii.gamebrowser.domain.game.single.Deal

@Composable
fun DealCard(deal: Deal) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RectangleShape
    ) {
        Column {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(4.dp), colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainer), border = BorderStroke(0.5.dp, Color.Yellow)){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://www.cheapshark.com/img/stores/banners/${deal.storeID}.png").crossfade(enable = true).build(),
                    contentDescription = "store banner",
                    placeholder = painterResource(R.drawable.placeholder),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Price:")
                Text(text = "$"+deal.price)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Retail price:")
                Text(text = "$"+deal.retailPrice )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Savings:")
                Text(text = "${deal.savings.toDouble().toInt()}%", color = Color.Green)
            }
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(4.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer), thickness = 0.5.dp
            )
        }
    }
}