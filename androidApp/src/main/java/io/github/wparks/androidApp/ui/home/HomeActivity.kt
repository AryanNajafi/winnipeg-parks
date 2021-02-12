package io.github.wparks.androidApp.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import io.github.wparks.androidApp.MyApplication
import io.github.wparks.androidApp.ui.MyApp

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp {
                Parks(viewModel = viewModel)
            }
        }
        
        val appContainer = (application as MyApplication).appContainer

        viewModelFactory = HomeViewModelFactory(appContainer.repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(HomeViewModel::class.java)

        viewModel.loadParks()
    }
}

@Composable
fun Parks(viewModel: HomeViewModel) {

    val viewState = viewModel.uiState.collectAsState()
    val lastIndex = viewState.value.parks.lastIndex

    Surface(Modifier.fillMaxSize()) {
        LazyColumn {
            itemsIndexed(viewState.value.parks) { index, park ->
                if (index == lastIndex) {
                    viewModel.loadMoreParks()
                }
                Text(text = park.title)
            }
        }
    }

}

@Preview("Park list preview")
@Composable
fun DefaultPreview() {
}