
package com.example.inventory.ui.viewModels

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem() {
        itemsRepository.insertItem(itemUiState.itemDetails.toItem())
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            country.isNotBlank() && city.isNotBlank() && summary.isNotBlank()
        }
    }



}

    data class ItemUiState(
        val itemDetails: ItemDetails = ItemDetails(),
        val isEntryValid: Boolean = false
    )

    data class ItemDetails(
        val id: Int = 0,
        val country: String = "",
        val city: String = "",
        val summary: String = "",
        var addedDate: Long = System.currentTimeMillis(),
        var rating: Int = 1,
        var image: Bitmap? = null
    )

    fun ItemDetails.toItem(): Item = Item(
        id = id,
        country = country,
        city = city,
        summary = summary,
        addedDate = addedDate,
        rating = rating,
        image = image
    )




    /**
     * Extension function to convert [Item] to [ItemUiState]
     */
    fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
        itemDetails = this.toItemDetails(),
        isEntryValid = isEntryValid
    )

    fun Item.toItemDetails(): ItemDetails = ItemDetails(
        id = id,
        country = country,
        city = city,
        summary = summary,
        addedDate = addedDate,
        rating = rating,
        image = image
    )


suspend fun loadBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // For Android API level 28 (P) and above
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                // For Android API level below 28
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
