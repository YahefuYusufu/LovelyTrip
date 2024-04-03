package com.example.inventory.components

import android.widget.RatingBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import com.example.inventory.R

@Composable
fun RatingInputRow(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    InputRow(
        inputLabel = stringResource(id = R.string.rating),
        modifier = modifier
    ){
        AndroidView(
            factory = { context ->
                RatingBar(context).apply {
                    stepSize = 1f
                }
            },
            update = { ratingBar ->
                ratingBar.rating = rating.toFloat()
                ratingBar.setOnRatingBarChangeListener { _, _, _ ->
                    onRatingChange(ratingBar.rating.toInt())
                }
            }
        )
    }
}

@Composable
fun InputRow(
    inputLabel: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = inputLabel,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier.weight(1f)
        )
        Box(modifier = Modifier.weight(2f)) {
            content()
        }
    }
}