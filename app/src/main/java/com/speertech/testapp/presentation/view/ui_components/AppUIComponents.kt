package com.speertech.testapp.presentation.view.ui_components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silverorange.videoplayer.R

@Composable
fun SearchButton(
    action: () -> Unit,
    text: String,
    modifier: Modifier,
) {
    Button(
        onClick = { action() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Green,
            contentColor = Color.White
        ),
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
        )
    }
}

@Composable
fun SearchTextField(
    hint: String,
    modifier: Modifier,
    fieldValue: MutableState<String>,
) {
    TextField(
        modifier = modifier,
        maxLines = 1,
        value = fieldValue.value ?: "",
        onValueChange = {
            fieldValue.value = it
        },
        label = {
            Text(
                text = hint,
                fontSize = 16.sp,
            )
        },
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.github),
                "search",
                tint = Blue
            )
        },
    )
}


@Composable
fun InfoText(
    modifier: Modifier,
    text: String = ""
) {
    Text(
        text = text,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color.DarkGray,
        modifier = modifier
    )
}