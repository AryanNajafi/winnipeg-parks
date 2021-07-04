package io.github.wparks.androidApp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.github.wparks.androidApp.R
import io.github.wparks.androidApp.ui.MyApp
import io.github.wparks.androidApp.ui.park.ParkActivity
import io.github.wparks.shared.Park
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp {
                val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
                val scope = rememberCoroutineScope()
                val uiState = viewModel.uiState.collectAsState()

                BackdropScaffold(
                    scaffoldState = scaffoldState,
                    gesturesEnabled = false,
                    backLayerBackgroundColor = colorResource(id = R.color.colorPrimary),
                    appBar = {
                        val appTitle = stringResource(id = R.string.app_name)
                        val filterTitle = stringResource(id = R.string.filter_title)
                        TopAppBar(
                            title = {
                                if (scaffoldState.isConcealed) {
                                    Text(text = appTitle)
                                } else {
                                    Text(text = filterTitle)
                                }
                                    },
                            backgroundColor = colorResource(id = R.color.colorPrimary),
                            elevation = 0.dp,
                            actions = {
                                IconButton(onClick = {
                                    if (scaffoldState.isConcealed) {
                                        scope.launch { scaffoldState.reveal() }
                                    } else {
                                        scope.launch { scaffoldState.conceal() }
                                    }
                                }) {
                                    Icon(
                                        Icons.Rounded.FilterList,
                                        stringResource(id = R.string.filter_action),
                                        tint = Color.White
                                    )
                                }
                            }
                        )
                             },
                    backLayerContent = {
                        uiState.value.assetTypes.forEach {
                            Text(text = it.title, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.height(200.dp))
                    },
                    frontLayerContent = {
                        Parks(viewModel = viewModel, onItemClick = {
                            val parkIntent = Intent(
                                this@HomeActivity, ParkActivity::class.java)
                            parkIntent.putExtra(INTENT_KEY_PARK_ID, it.id)
                            parkIntent.putExtra(INTENT_KEY_PARK_TITLE, it.title)
                            startActivity(parkIntent)
                        })
                    }) {

                }
            }
        }

        viewModel.loadParks()
    }

    companion object {
        const val INTENT_KEY_PARK_ID = "park_id"
        const val INTENT_KEY_PARK_TITLE = "park_title"
    }
}

@Composable
fun Parks(viewModel: HomeViewModel,
          onItemClick: (Park) -> Unit) {

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