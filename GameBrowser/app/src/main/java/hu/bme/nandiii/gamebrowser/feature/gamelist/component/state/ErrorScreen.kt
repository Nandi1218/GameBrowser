package hu.bme.nandiii.gamebrowser.feature.gamelist.component.state

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorScreen(text: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

}