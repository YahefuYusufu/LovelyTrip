
package com.lovelytrip.lovelyTrip.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovelytrip.lovelyTrip.data.ItemsRepository
import com.lovelytrip.lovelyTrip.ui.screens.ItemEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    //Here, getItemStream(itemId) is responsible for fetching the item based on the itemId. The filterNotNull() is used to filter out any null values, and first() is used to get the first emitted item from the stream. The fetched item is then converted to an ItemUiState using .toItemUiState(true).
    init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }


    suspend fun updateItem() {
            if (validateInput(itemUiState.itemDetails)) {
                itemsRepository.updateItem(itemUiState.itemDetails.toItem())
            }
    }


    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails,isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            country.isNotBlank() && city.isNotBlank() && summary.isNotBlank()
        }
    }
}
