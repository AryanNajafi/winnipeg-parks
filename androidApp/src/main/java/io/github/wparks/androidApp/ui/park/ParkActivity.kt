package io.github.wparks.androidApp.ui.park

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import io.github.wparks.androidApp.ui.MyApp
import io.github.wparks.androidApp.ui.home.HomeActivity
import io.github.wparks.shared.Asset

@AndroidEntryPoint
class ParkActivity : AppCompatActivity() {

    private val viewModel: ParkViewModel by viewModels()

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
    val viewState: ParkViewState by viewModel.uiState.collectAsState()

    val typography = MaterialTheme.typography
    
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.surface)
    ) {
        Column {
            Box(modifier = Modifier.height(200.dp)) {
                if (viewState.park != null) {
                    ParkMapView(viewState.assets)
                }
            }

            Spacer(modifier = Modifier.requiredHeight(10.dp))
            Text(modifier = Modifier.padding(horizontal = 10.dp),
                text = "Amenities", style = typography.h6)
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                itemsIndexed(
                    viewState.assets.distinctBy {
                        AssetSelector(it.typeId, it.subtype, it.size)
                    }
                ) { index, asset ->
                    AssetCard(asset = asset)
                }
            }
        }

    }

}

@Composable
private fun ParkMapView(assets: List<Asset>) {
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, assets = assets)
}

@Composable
private fun MapViewContainer(
    map: MapView,
    assets: List<Asset>
) {
    var mapInitialized by remember(map) { mutableStateOf(false) }
    LaunchedEffect(map, mapInitialized) {
        if (!mapInitialized) {
            val googleMap = map.awaitMap()
            val latLngBoundsBuilder = LatLngBounds.Builder()
            assets
                .forEach { asset ->
                    val position = LatLng(asset.latitude!!, asset.longitude!!)
                    googleMap.addMarker {
                        position(position)
                        title(asset.title)
                        snippet(asset.subtype)
                    }
                    latLngBoundsBuilder.include(position)
                }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                latLngBoundsBuilder.build(), 20))
            mapInitialized = true
        }
    }

    AndroidView({ map }) { mapView ->

    }
}

data class AssetSelector(val typeId: Long, val subType: String?, val size: String?)