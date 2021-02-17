package io.github.wparks.androidApp.ui.park

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.wparks.shared.Asset
import io.github.wparks.shared.Park
import io.github.wparks.shared.data.ParkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ParkViewModel(private val repository: ParkRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ParkViewState())
    val uiState: StateFlow<ParkViewState> get() = _uiState

    private val parkId = MutableStateFlow(0L)

    init {
        viewModelScope.launch {
            parkId.collectLatest {
                if (it != 0L) {
                    val park = repository.loadParkInfo(it)
                    repository.loadParkAssets(it).collect { assets ->
                        _uiState.value = ParkViewState(park, assets)
                    }
                }
            }
        }
    }

    fun loadParkInfo(id: Long) {
        parkId.value = id
    }
}

data class ParkViewState(
    val park: Park? = null,
    val assets: List<Asset> = emptyList(),
    val loading: Boolean = false
)