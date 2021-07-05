package io.github.wparks.androidApp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.wparks.shared.Park
import io.github.wparks.shared.data.AssetType
import io.github.wparks.shared.data.ParkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val parkRepository: ParkRepository
    ) : ViewModel() {

    private var currentPage: Long = -1

    private val parkParamsFlow = MutableStateFlow(ParkParams())

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> get() = _uiState

    private val _filterUiState = MutableStateFlow(FilterViewState())
    val filterUiState: StateFlow<FilterViewState> get() = _filterUiState

    init {
        viewModelScope.launch {
            parkRepository.tryUpdateParksCache()

            launch {
                parkRepository.loadAssetTypes().collect { assetTypes ->
                    _filterUiState.value = FilterViewState(assetTypes = assetTypes)
                }
            }
            
            parkParamsFlow.collectLatest { parkParams ->
                parkRepository.loadParks(parkParams.page, parkParams.filterIds).collect { parks ->
                    _uiState.value = HomeViewState(_uiState.value.parks + parks)
                    currentPage++
                }
            }

        }
    }

    fun loadParks() {
        parkParamsFlow.value = ParkParams()
    }

    fun loadMoreParks() {
        parkParamsFlow.value = ParkParams(currentPage + 1, parkParamsFlow.value.filterIds)
    }

    fun setFilters(filterIds: List<Long>) {
        _uiState.value = HomeViewState()
        parkParamsFlow.value = ParkParams(filterIds = filterIds)
    }
}

data class HomeViewState(
    val parks: List<Park> = emptyList(),
    val loading: Boolean = false,
)

data class FilterViewState(
    val assetTypes: List<AssetType> = emptyList(),
    val loading: Boolean = false
)

data class ParkParams(
    val page: Long = 0,
    val filterIds: List<Long> = emptyList()
)

