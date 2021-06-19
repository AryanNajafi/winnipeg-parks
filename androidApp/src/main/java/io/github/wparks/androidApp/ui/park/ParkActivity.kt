package io.github.wparks.androidApp.ui.park

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        val parkTitle = intent.getStringExtra(HomeActivity.INTENT_KEY_PARK_TITLE)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title = parkTitle
        }

        val appContainer = (application as MyApplication).appContainer

        viewModelFactory = ParkViewModelFactory(appContainer.repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ParkViewModel::class.java)

        viewModel.loadParkInfo(parkId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

@Composable
fun ParkInfo(viewModel: ParkViewModel) {

    val viewState = viewModel.uiState.collectAsState()

    val typography = MaterialTheme.typography
    
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.surface)
    ) {
        Column(Modifier.padding(10.dp) ) {
            Text(text = "Amenities", style = typography.h6)
            Spacer(modifier = Modifier.requiredHeight(10.dp))
            LazyColumn {
                itemsIndexed(
                    viewState.value.assets.distinctBy {
                        AssetSelector(it.typeId, it.subtype, it.size)
                    }
                ) { index, asset ->
                    AssetCard(asset = asset)
                }
            }
        }

    }

}

data class AssetSelector(val typeId: Long, val subType: String?, val size: String?)