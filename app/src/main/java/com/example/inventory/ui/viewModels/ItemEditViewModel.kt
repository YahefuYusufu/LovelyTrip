
package com.example.inventory.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import com.example.inventory.ui.screens.ItemEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository
) : ViewModel() {


    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState()
        }
    }


    suspend fun updateItem() {
            itemsRepository.updateItem(itemUiState.itemDetails.toItem())
    }


    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails)
    }

//    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
//        return with(uiState) {
//            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
//        }
//    }
}
