package io.github.wparks.androidApp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import io.github.wparks.androidApp.MyApplication
import io.github.wparks.androidApp.ui.MyApp
import io.github.wparks.androidApp.ui.park.ParkActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp {
                Parks(viewModel = viewModel, onItemClick = {
                    val parkIntent = Intent(this, ParkActivity::class.java)
                    parkIntent.putExtra(INTENT_KEY_PARK_ID, it)
                    startActivity(parkIntent)
                })
            }
        }
        
        val appContainer = (application as MyApplication).appContainer

        viewModelFactory = HomeViewModelFactory(appContainer.repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(HomeViewModel::class.java)

        viewModel.loadParks()
    }

    companion object {
        public const val INTENT_KEY_PARK_ID = "park_id"
    }
}

@Composable
fun Parks(viewModel: HomeViewModel,
          onItemClick: (Long) -> Unit) {

    val viewState = viewModel.uiState.collectAsState()
    val lastIndex = viewState.value.parks.lastIndex

    Surface(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.background(color = Color(0xFFF3F3F3))) {
            itemsIndexed(viewState.value.parks) { index, park ->
                if (index == lastIndex) {
                    viewModel.loadMoreParks()
                }
                ParkCard(park = park, onItemClick = onItemClick)
            }
        }
    }

}

@Preview("Park list preview")
@Composable
fun DefaultPreview() {
}