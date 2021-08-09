package io.github.wparks.androidApp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import dagger.hilt.android.AndroidEntryPoint
import io.github.wparks.androidApp.R
import io.github.wparks.androidApp.data.Filter
import io.github.wparks.androidApp.ui.MyApp
import io.github.wparks.androidApp.ui.components.FilterChip
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
                val filterUiState: FilterViewState by viewModel.filterUiState.collectAsState()

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
                        val filters = filterUiState.assetTypes.map { Filter(it.id, it.title) }
                        FlowRow(modifier = Modifier.padding(5.dp)) {
                                filters.forEach { filter ->
                                    FilterChip(filter = filter) {

                                    }
                                }
                        }

                        Row(modifier = Modifier.padding(10.dp)) {
                            Button(modifier = Modifier.weight(1f), onClick = {
                                if (filters.any { it.checked.value }) {
                                    scope.launch { scaffoldState.conceal() }
                                    viewModel.setFilters(filters
                                        .filter { it.checked.value }
                                        .map { it.id.toLong() })
                                }
                            }) {
                                Text(text = stringResource(id = R.string.filter_submit))
                            }

                            Spacer(modifier = Modifier.width(15.dp))

                            Button(modifier = Modifier.weight(1f), onClick = {
                                scope.launch { scaffoldState.conceal() }
                            }) {
                                Text(text = stringResource(id = R.string.filter_cancel))
                            }
                        }
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

    val viewState: HomeViewState by viewModel.uiState.collectAsState()
    val lastIndex = viewState.parks.lastIndex

    Surface(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.background(color = Color(0xFFF3F3F3))) {
            if (viewState.loading.not()) {
                itemsIndexed(viewState.parks) { index, park ->
                    if (index == lastIndex) {
                        viewModel.loadMoreParks()
                    }
                    ParkCard(park = park, onItemClick = onItemClick)
                }
            } else {
                items(10) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colors.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp, 10.dp)) {
                            Text(
                                text = "Lorem ipsum dolor sit amet",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.placeholder(
                                    visible = viewState.loading,
                                    highlight = PlaceholderHighlight.shimmer()
                                )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Lorem ipsum dolor sit",
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.placeholder(
                                    visible = viewState.loading,
                                    highlight = PlaceholderHighlight.shimmer()
                                )
                            )
                        }
                    }
                }
            }


        }
    }

}

@Preview("Park list preview")
@Composable
fun DefaultPreview() {
}