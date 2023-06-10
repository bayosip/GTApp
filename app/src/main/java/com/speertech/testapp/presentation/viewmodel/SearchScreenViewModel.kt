package com.speertech.testapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.presentation.usecases.SearchUseCase
import com.speertech.testapp.presentation.view.screens.screen_states.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val TAG = javaClass.canonicalName
    private val state: MutableState<SearchScreenState> = mutableStateOf(SearchScreenState())
    val _state: State<SearchScreenState> = state


    fun getSearchForUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchUseCase(username).cachedIn(viewModelScope).collectLatest { data ->
                withContext(Dispatchers.Main) {
                    state.value = _state.value.copy(
                        pagingData = flow { emit(data) },
                        isLoading = false,
                    )
                }
            }
        }
    }
}