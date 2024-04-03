
package com.example.inventory.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository

class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails)
    }

    suspend fun saveItem() {

        itemsRepository.insertItem(itemUiState.itemDetails.toItem())
    }

}

    data class ItemUiState(
        val itemDetails: ItemDetails = ItemDetails(),
    )

    data class ItemDetails(
        val id: Int = 0,
        val country: String = "",
        val city: String = "",
        val summary: String = "",
        var addedDate: Long = System.currentTimeMillis(),
        var rating: Int = 0
    )

    fun ItemDetails.toItem(): Item = Item(
        id = id,
        country = country,
        city = city,
        summary = summary,
        addedDate = addedDate,
        rating = rating
    )


    /**
     * Extension function to convert [Item] to [ItemUiState]
     */
    fun Item.toItemUiState(): ItemUiState = ItemUiState(
        itemDetails = this.toItemDetails(),
    )

    fun Item.toItemDetails(): ItemDetails = ItemDetails(
        id = id,
        country = country,
        city = city,
        summary = summary,
        addedDate = addedDate,
        rating = rating
    )
