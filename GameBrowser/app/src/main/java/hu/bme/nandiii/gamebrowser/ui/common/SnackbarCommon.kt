package hu.bme.nandiii.gamebrowser.ui.common

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SnackbarCommon(
    snackbarHostState: SnackbarHostState,
    cardModifier: Modifier = Modifier
        .padding(bottom = 16.dp)
        .windowInsetsPadding(insets = WindowInsets.safeDrawing),
    textModifier: Modifier = Modifier.padding(8.dp),
    backgroundColor: CardColors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.secondaryContainer),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    contentStyle: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    SnackbarHost(hostState = snackbarHostState, snackbar = {
        ElevatedCard(
            colors = backgroundColor,
            elevation = elevation,
            modifier = cardModifier
        ) {
            Log.d("AUTH", "Inside SnackbarCommon: ${it.visuals.message}")
            Text(
                text = it.visuals.message,
                style = contentStyle,
                color = contentColor,
                modifier = textModifier
            )
        }
    })
}