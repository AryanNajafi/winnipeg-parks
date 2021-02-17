package io.github.wparks.androidApp.ui.park

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.wparks.shared.data.ParkRepository

class ParkViewModelFactory(private val parkRepository: ParkRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParkViewModel::class.java)) {
            return ParkViewModel(parkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}