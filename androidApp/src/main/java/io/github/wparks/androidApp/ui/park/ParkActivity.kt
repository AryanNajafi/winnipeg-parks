package io.github.wparks.androidApp.ui.park

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import io.github.wparks.androidApp.R
import io.github.wparks.androidApp.ui.MyApp
import io.github.wparks.androidApp.ui.home.HomeActivity
import io.github.wparks.shared.Asset

@AndroidEntryPoint
class ParkActivity : AppCompatActivity() {

    private val viewModel: ParkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
            }
            MyApp {
                ProvideWindowInsets {
                    ParkInfo(viewModel, onBackPressed = { finish() })
                }
            }
        }

        val parkId = intent.getLongExtra(HomeActivity.INTENT_KEY_PARK_ID, -1)

        viewModel.loadParkInfo(parkId)
    }

}

@Composable
fun ParkInfo(viewModel: ParkViewModel, onBackPressed: () -> Unit) {
    val viewState: ParkViewState by viewModel.uiState.collectAsState()

    val typography = MaterialTheme.typography
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                backgroundColor = Color.Transparent,
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                    applyBottom = false
                ),
                elevation = 0.dp,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) {
        Column {
            Box(modifier = Modifier.height(300.dp)) {
                viewState.park?.let { park ->
                    ParkMapView(viewState.assets, LatLng(park.latitude, park.longitude))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x5C181818))
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = park.title,
                            color = Color.White,
                            style = MaterialTheme.typography.subtitle1)
                        Text(
                            text = park.address,
                            color = Color.White,
                            style = MaterialTheme.typography.subtitle2)
                    }
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
private fun ParkMapView(assets: List<Asset>, parkPosition: LatLng) {
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, assets = assets, parkPosition = parkPosition)
}

@Composable
private fun MapViewContainer(
    map: MapView,
    parkPosition: LatLng,
    assets: List<Asset>
) {
    var mapInitialized by remember(map) { mutableStateOf(false) }
    val topPadding = with(LocalDensity.current) { 50.dp.toPx().toInt() }
    val bottomPadding = with(LocalDensity.current) { 50.dp.toPx().toInt() }
    val cameraPadding = with(LocalDensity.current) { 30.dp.toPx().toInt() }

    val context = LocalContext.current

    LaunchedEffect(map, mapInitialized) {
        if (!mapInitialized) {
            val googleMap = map.awaitMap()
            googleMap.setPadding(0, topPadding, 0, bottomPadding)
            if (assets.isNotEmpty()) {
                val latLngBoundsBuilder = LatLngBounds.Builder()
                assets
                    .forEach { asset ->
                        val position = LatLng(asset.latitude, asset.longitude)
                        googleMap.addMarker {
                            position(position)
                            title(asset.title)
                            snippet(asset.subtype)
                            icon(BitmapDescriptorFactory.fromBitmap(
                                getMarkerIcon(context = context, assetId = asset.typeId)))
                        }
                        latLngBoundsBuilder.include(position)
                    }

                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        latLngBoundsBuilder.build(), cameraPadding
                    )
                )
            } else {
                googleMap.addMarker {
                    position(parkPosition)
                }
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(parkPosition, 16F)
                )
            }
            mapInitialized = true
        }
    }

    AndroidView({ map }) { mapView ->

    }
}

private fun getMarkerIcon(context: Context, assetId: Long): Bitmap {
    val resId = when (assetId.toInt()) {
        100 -> R.drawable.map_marker_seat
        101 -> R.drawable.map_marker_sailing
        102 -> R.drawable.map_marker_bike
        103 -> R.drawable.map_marker_monuement
        104 -> R.drawable.map_marker_grill
        105 -> R.drawable.map_marker_shelter
        106 -> R.drawable.map_marker_sledding
        107 -> R.drawable.map_marker_parking
        108 -> R.drawable.map_marker_park
        109 -> R.drawable.map_marker_ice_skating
        110 -> R.drawable.map_marker_skateboarding
        111 -> R.drawable.map_marker_pets
        112 -> R.drawable.map_marker_park
        113 -> R.drawable.map_marker_park
        114 -> R.drawable.map_marker_park
        115 -> R.drawable.map_marker_fitness
        116 -> R.drawable.map_marker_skiing
        117 -> R.drawable.map_marker_soccer
        118 -> R.drawable.map_marker_football
        119 -> R.drawable.map_marker_baseball
        120 -> R.drawable.map_marker_rugby
        121 -> R.drawable.map_marker_circle
        122 -> R.drawable.map_marker_sports
        123 -> R.drawable.map_marker_sports
        124 -> R.drawable.map_marker_sports
        125 -> R.drawable.map_marker_cricket
        126 -> R.drawable.map_marker_sports
        127 -> R.drawable.map_marker_handball
        128 -> R.drawable.map_marker_basketball
        129 -> R.drawable.map_marker_tennis
        130 -> R.drawable.map_marker_tennis
        131 -> R.drawable.map_marker_volleyball
        132 -> R.drawable.map_marker_football
        133 -> R.drawable.map_marker_sports
        134 -> R.drawable.map_marker_running
        135 -> R.drawable.map_marker_sports
        136 -> R.drawable.map_marker_sports
        else -> R.drawable.map_marker_park
    }
    return requireNotNull(AppCompatResources.getDrawable(context, resId)).toBitmap()
}

data class AssetSelector(val typeId: Long, val subType: String?, val size: String?)