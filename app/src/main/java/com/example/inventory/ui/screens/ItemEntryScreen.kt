
package com.example.inventory.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.TopAppBar
import com.example.inventory.R
import com.example.inventory.components.ItemDatePicker
import com.example.inventory.components.RatingInputRow
import com.example.inventory.components.changeMillisToDateString
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.viewModels.ItemDetails
import com.example.inventory.ui.viewModels.ItemEntryViewModel
import com.example.inventory.ui.viewModels.ItemUiState
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.launch
import java.time.Instant

object ItemEntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.item_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(ItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {

                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDataPickerDialogOpen by rememberSaveable  { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )
    val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        ItemDatePicker(
            state = datePickerState,
            isOpen = isDataPickerDialogOpen,
            onDismissRequest = {
                isDataPickerDialogOpen = false
            },
            onConfirmButtonClicked = {
                onItemValueChange(itemUiState.itemDetails.copy(addedDate = selectedDate))
                isDataPickerDialogOpen = false

            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = datePickerState.selectedDateMillis.changeMillisToDateString(),
                style = MaterialTheme.typography.bodyLarge,
            )

            IconButton(
                onClick = {
                    isDataPickerDialogOpen = true
                }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            }
        }
        ItemInputForm(
            itemDetails = itemUiState.itemDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}



@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            OutlinedTextField(
                value = itemDetails.country,
                onValueChange = { onValueChange(itemDetails.copy(country = it)) },
                label = { Text(stringResource(R.string.country)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.weight(1f),
                enabled = enabled,
                singleLine = true
            )
            OutlinedTextField(
                value = itemDetails.city,
                onValueChange = { onValueChange(itemDetails.copy(city = it)) },
                label = { Text(stringResource(R.string.city)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                ,
                enabled = enabled,
                singleLine = true
            )
        }
        OutlinedTextField(
            value = itemDetails.summary,
            onValueChange = { onValueChange(itemDetails.copy(summary = it)) },
            label = { Text(stringResource(R.string.summary)) },
            minLines = 3,
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false,
        )
        RatingInputRow(
            rating = itemDetails.rating,
            onRatingChange = {rating -> onValueChange(itemDetails.copy(rating = rating ))} )
    }

}

@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    InventoryTheme {
        ItemEntryBody(itemUiState = ItemUiState(
            ItemDetails(
                country = "Sweden", city = "Malmo", summary = "Visited to a lot place.", addedDate = 1233
            )
        ), onItemValueChange = {}, onSaveClick = {})
    }
}



//@Composable
//fun ItemInputForm(
//    itemDetails: ItemDetails,
//    modifier: Modifier = Modifier,
//    onValueChange: (ItemDetails) -> Unit = {},
////    enabled: Boolean = true
//) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
//    ) {
//        OutlinedTextField(
//            value = itemDetails.name,
//            onValueChange = { onValueChange(itemDetails.copy(name = it)) },
//            label = { Text(stringResource(R.string.item_name_req)) },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            ),
//            modifier = Modifier.fillMaxWidth(),
////            enabled = enabled,
//            singleLine = true
//        )
//        OutlinedTextField(
//            value = itemDetails.price,
//            onValueChange = { onValueChange(itemDetails.copy(price = it)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//            label = { Text(stringResource(R.string.item_price_req)) },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            ),
//            leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
//            modifier = Modifier.fillMaxWidth(),
////            enabled = enabled,
//            singleLine = true
//        )
//        OutlinedTextField(
//            value = itemDetails.quantity,
//            onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            label = { Text(stringResource(R.string.quantity_req)) },
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//            ),
//            modifier = Modifier.fillMaxWidth(),
////            enabled = enabled,
//            singleLine = true
//        )
////        if (enabled) {
////            Text(
////                text = stringResource(R.string.required_fields),
////                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
////            )
////        }
//    }
//}