package com.speertech.testapp.presentation.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.presentation.view.screens.screen_states.SearchScreenState
import com.speertech.testapp.presentation.view.toolbar.ExpandedTopBar
import com.speertech.testapp.presentation.view.ui_components.ErrorItem
import com.speertech.testapp.presentation.view.ui_components.UserResultListItem

@Composable
fun SearchScreen(
    navigateToUserScreen: (user: String) -> Unit = {},
    hasSearchStarted: State<Boolean>,
    searchScreenState: State<SearchScreenState>,
    searchAction: (username: String) -> Unit,
) {
    Log.d("TAG", "SearchScreen: ${hasSearchStarted.value}")
    val input = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ExpandedTopBar(
            input = input,
            action = searchAction,
            searchStarted = hasSearchStarted,
        )
        if (hasSearchStarted.value) {
            SearchResultList(
                searchScreenState = searchScreenState,
                onClick = navigateToUserScreen
            )
        }
    }
}

@Composable
fun SearchResultList(
    searchScreenState: State<SearchScreenState>,
    onClick: (username: String) -> Unit,
) {
    val data = searchScreenState.value.pagingData?.collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = data?.itemSnapshotList?.items?.toSet()?.toList() ?: emptyList(),
            key = { item: FollowModel ->
                try {
                    item.id
                } catch (e: IllegalArgumentException) {
                    Log.e("TAG", "SearchResultList: ", e)
                }
            }
        ) {
            UserResultListItem(
                item = it,
                onItemClicked = onClick,
            )
        }

        when (val state = data?.loadState?.refresh) { //FIRST LOAD
            is LoadState.Error -> {
                //TODO Error Item
                if (data.itemSnapshotList.items.isEmpty())
                    item {
                        ErrorItem()
                    }
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
                if (data.itemSnapshotList.items.isEmpty())
                    item {
                        ErrorItem()
                    }
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

                        CircularProgressIndicator(color = Color.Blue,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                }
            }

            else -> {}
        }
    }
}
