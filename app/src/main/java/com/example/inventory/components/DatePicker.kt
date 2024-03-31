package com.example.inventory.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.room.TypeConverter

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    state: DatePickerState,
    isOpen: Boolean,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: () -> Unit
) {
    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirmButtonClicked) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = dismissButtonText)
                }
            }
        ) {
            DatePicker(
                state = state
            )
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//fun Long?.changeMillisToDateString(): String {
//    val date: LocalDate = this?.let {
//        Instant
//            .ofEpochMilli(it)
//            .atZone(ZoneId.systemDefault())
//            .toLocalDate()
//    } ?: LocalDate.now()
//    return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
//}

class DateConverter {
    @TypeConverter
    fun timeToString(date: Long?): Date? {
         return date?.let { Date(it) }
    }

     @TypeConverter
    fun stringToTime(date: Date?): Long? {
        return date?.time
    }

}