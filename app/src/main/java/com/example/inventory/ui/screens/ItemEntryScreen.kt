
package com.example.inventory.ui.screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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

@Composable
fun ItemEntryBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {


    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {

        ItemInputForm(
            itemDetails = itemUiState.itemDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = itemUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {},
    enabled: Boolean = true
) {
    var isDataPickerDialogOpen by rememberSaveable  { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )
    val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
        }
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            OutlinedTextField(
                value = itemDetails.country,
                onValueChange = {newCountry -> onValueChange(itemDetails.copy(country = newCountry)) },
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
                onValueChange = { newCity -> onValueChange(itemDetails.copy(city = newCity)) },
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
            onValueChange = { newSummary -> onValueChange(itemDetails.copy(summary = newSummary)) },
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
            onRatingChange = {newRating -> onValueChange(itemDetails.copy(rating = newRating ))} )

        //Date Picker
        ItemDatePicker(
            state = datePickerState,
            isOpen = isDataPickerDialogOpen,
            onDismissRequest = {
                isDataPickerDialogOpen = false
            },
            onConfirmButtonClicked = {
                onValueChange(itemDetails.copy(addedDate = selectedDate))
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

        //Image Picker
       Column(
           modifier = Modifier.fillMaxWidth(),
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Button(
               modifier = Modifier.height(46.dp),
               shape = RoundedCornerShape(8.dp),
               colors = ButtonDefaults.buttonColors(
                   containerColor = MaterialTheme.colorScheme.primary,
                   contentColor = MaterialTheme.colorScheme.onSecondary
               ),
               onClick = {
                   photoPickerLauncher.launch(
                       PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                   )
               }) {
               Row {
                   Icon(imageVector = Icons.Default.Add, contentDescription = null)
                   Text(
                       "Pick a Photo",
                       style = TextStyle(fontSize = 18.sp)
                   )
               }

               AsyncImage(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(240.dp)
                       .clip(RoundedCornerShape(R.dimen.padding_small))
                   ,
                   model = selectedImageUri,
                   contentDescription = null,
                   contentScale = ContentScale.FillBounds
                   )
           }


       }
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



