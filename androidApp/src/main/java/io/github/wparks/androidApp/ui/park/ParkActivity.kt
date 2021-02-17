package io.github.wparks.androidApp.ui.park

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import io.github.wparks.androidApp.MyApplication
import io.github.wparks.androidApp.ui.MyApp
import io.github.wparks.androidApp.ui.home.HomeActivity

class ParkActivity : AppCompatActivity() {

    private lateinit var viewModel: ParkViewModel
    private lateinit var viewModelFactory: ParkViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp {
                ParkInfo(viewModel)
            }
        }
        val parkId = intent.getLongExtra(HomeActivity.INTENT_KEY_PARK_ID, -1)

        val appContainer = (application as MyApplication).appContainer

        viewModelFactory = ParkViewModelFactory(appContainer.repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParkViewModel::class.java)

        viewModel.loadParkInfo(parkId)
    }

}

@Composable
fun ParkInfo(viewModel: ParkViewModel) {

    val viewState = viewModel.uiState.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.background(color = Color(0xFFF3F3F3))) {
            itemsIndexed(viewState.value.assets) { index, asset ->
                Text(text = asset.title, style = MaterialTheme.typography.body2)
            }
        }
    }

}
