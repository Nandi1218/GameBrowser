package hu.bme.nandiii.gamebrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import hu.bme.nandiii.gamebrowser.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.nandiii.gamebrowser.navigation.NavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                NavGraph()
            }
        }
    }

}
