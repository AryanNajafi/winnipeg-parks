package io.github.wparks.androidApp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.wparks.shared.Park
import io.github.wparks.shared.data.AssetType
import io.github.wparks.shared.data.ParkRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val parkRepository: ParkRepository
    ) : ViewModel() {

    private var currentPage: Long = -1

    private val pageNumberFlow = MutableStateFlow(0L)

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> get() = _uiState

    init {
        viewModelScope.launch {
            parkRepository.tryUpdateParksCache()
            val assetTypesFlow = parkRepository.loadAssetTypes()
            pageNumberFlow.collectLatest { pageNumber ->
                val parksFlow = parkRepository.loadParks(pageNumber)
                combine(parksFlow, assetTypesFlow) { parks, assetTypes ->
                    HomeViewState(_uiState.value.parks + parks, assetTypes)
                }.collect {
                    _uiState.value = it
                    currentPage++
                }
            }
        }
    }

    fun loadParks() {
        pageNumberFlow.value = 0
    }

    fun loadMoreParks() {
        pageNumberFlow.value = currentPage + 1
    }
}

data class HomeViewState(
    val parks: List<Park> = emptyList(),
    val assetTypes: List<AssetType> = emptyList(),
    val loading: Boolean = false,
)

