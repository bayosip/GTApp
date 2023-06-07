package com.speertech.testapp.presentation.view.screens

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import java.lang.Float.min

@Composable
fun ProfileScreen() {

    val scrollState = rememberLazyListState()
    val scrollOffset: Float = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )

}