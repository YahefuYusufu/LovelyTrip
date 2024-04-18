
package com.lovelytrip.lovelyTrip.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lovelytrip.lovelyTrip.LovelyTripApplication
import com.lovelytrip.lovelyTrip.ui.viewModels.HomeViewModel
import com.lovelytrip.lovelyTrip.ui.viewModels.ItemDetailsViewModel
import com.lovelytrip.lovelyTrip.ui.viewModels.ItemEditViewModel
import com.lovelytrip.lovelyTrip.ui.viewModels.ItemEntryViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            ItemEditViewModel(
                this.createSavedStateHandle(),
                lovelyTripApplication().container.itemsRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(lovelyTripApplication().container.itemsRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                lovelyTripApplication().container.itemsRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(lovelyTripApplication().container.itemsRepository)
        }
    }
}


fun CreationExtras.lovelyTripApplication(): LovelyTripApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LovelyTripApplication)
