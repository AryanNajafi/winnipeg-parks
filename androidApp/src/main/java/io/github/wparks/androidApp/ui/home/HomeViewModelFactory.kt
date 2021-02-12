package io.github.wparks.androidApp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.wparks.shared.data.ParkRepository
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val parkRepository: ParkRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(parkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}