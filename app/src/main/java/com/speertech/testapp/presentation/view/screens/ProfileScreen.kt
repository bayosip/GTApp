package com.speertech.testapp.presentation.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.speertech.testapp.model.inWords
import com.speertech.testapp.presentation.view.screens.screen_states.ProfileScreenState
import com.speertech.testapp.presentation.view.toolbar.ProfileCollapsedTopBar
import com.speertech.testapp.presentation.view.ui_components.UserResultListItem
import com.speertech.testapp.presentation.view.ui_components.onClick
import com.speertech.testapp.repository.FollowsDesignation
import java.lang.Float.min

@Composable
fun ProfileScreen(
    username: String,
    screenState: State<ProfileScreenState>,
    launchQuery: (username: String) -> Unit,
    scrollOffset: MutableState<Float>,
    checkFollowsAction: (Int) -> Unit,
) {
//    val isFollowClicked = remember {
//        mutableStateOf(false)
//    }

    val scrollState = rememberLazyListState()
    scrollOffset.value = min(
        1f,
        1 - (scrollState.firstVisibleItemScrollOffset / 600f + scrollState.firstVisibleItemIndex)
    )

    Column(modifier = Modifier.fillMaxSize()) {

        ProfileCollapsedTopBar(
            scrollOffset = scrollOffset,
            user = screenState.value.user,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Outlined.Person,
                contentDescription = "follows",
                tint = Color(0xffd8e6ff)
            )
            Text(
                modifier = Modifier
                    .onClick {
                        if (screenState.value.user?.followers!! > 0)
                            checkFollowsAction(FollowsDesignation.FOLLOWERS)
                    }
                    .padding(end = 32.dp),
                text = (screenState.value.user?.followers?.inWords() ?: "0")
                    .plus(" Followers"),
                fontSize = 20.sp,
            )
            Text(
                modifier = Modifier.onClick {
                    if (screenState.value.user?.following!! > 0)
                        checkFollowsAction(FollowsDesignation.FOLLOWING)
                },
                text = (screenState.value.user?.following?.inWords() ?: "0")
                    .plus(" Following"),
                fontSize = 20.sp,
            )
        }

        if (screenState.value.follows?.isNotEmpty() !=null) {
            Log.d("TAG", "ProfileScreen: ${screenState.value.follows?.get(0)}")
            LazyColumn(
                state = scrollState,
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                Log.d("TAG", "ProfileScreen: empty")
                items(items = screenState.value.follows?: emptyList(),
                    itemContent = { item ->
                        UserResultListItem(item = item,
                            onItemClicked = { launchQuery(item.username) })
                    })
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Click on 'followers' or 'following' to see follows",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp
                )
            }
        }
    }


    LaunchedEffect(key1 = username) {
        launchQuery(username)
    }
}