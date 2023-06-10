package com.speertech.testapp.presentation.view.screens.screen_states

import androidx.paging.PagingData
import com.speertech.testapp.model.FollowModel
import kotlinx.coroutines.flow.Flow

data class SearchScreenState(
    val isLoading: Boolean = false,
    val pagingData: Flow<PagingData<FollowModel>>?=null,
)