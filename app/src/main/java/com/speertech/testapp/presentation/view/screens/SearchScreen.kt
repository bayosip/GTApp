package com.speertech.testapp.presentation.view.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.presentation.view.screens.screen_states.SearchScreenState
import com.speertech.testapp.presentation.view.ui_components.SearchResultListItem

@Composable
fun SearchScreen(
    navigateToUserScreen: (user: String) -> Unit = {},
    hasSearchStarted: State<Boolean>,
    screenState: State<SearchScreenState>,
) {
    Log.d("TAG", "SearchScreen: ${hasSearchStarted.value}")

    AnimatedVisibility(visible = hasSearchStarted.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SearchResultList(
                screenState = screenState,
                onClick = navigateToUserScreen
            )
        }
    }
}

@Composable
fun SearchResultList(
    screenState: State<SearchScreenState>,
    onClick: (username: String) -> Unit,
) {
    val data = screenState.value.pagingData?.collectAsLazyPagingItems()
    LazyColumn {
        items(
            items = data?.itemSnapshotList?.items ?: emptyList(),
            key = { item: FollowModel ->
                item.id
            }
        ) {
            SearchResultListItem(
                item = it,
                onItemClicked = onClick,
            )
        }

        when (val state = data?.loadState?.refresh) { //FIRST LOAD
            is LoadState.Error -> {
                //TODO Error Item
                //state.error to get error message
            }

            is LoadState.Loading -> { // Loading UI
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp),
                            text = "Refresh Loading"
                        )
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
            }

            else -> {}
        }

        when (val state = data?.loadState?.append) { // Pagination
            is LoadState.Error -> {
                //TODO Pagination Error Item
                //state.error to get error message
            }

            is LoadState.Loading -> { // Pagination Loading UI
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = "Pagination Loading")

                        CircularProgressIndicator(
                            modifier = Modifier
                                .background(Color.Blue)
                                .padding(16.dp)
                        )
                    }
                }
            }

            else -> {}
        }
    }
}