package com.speertech.testapp.presentation.view.toolbar

import androidx.compose.runtime.Stable

@Stable
interface ToolState {
    val offset: Float
    val height: Float
    val progress: Float
    var scrollValue: Int
}