package com.speertech.testapp.presentation.view.toolbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.silverorange.videoplayer.R
import com.speertech.testapp.model.User
import com.speertech.testapp.presentation.view.screens.ScreenNames
import com.speertech.testapp.presentation.view.ui_components.SearchTextField
import java.lang.Float.max

val COLLAPSED_TOP_BAR_HEIGHT = 90.dp
val EXPANDED_TOP_BAR_HEIGHT = 550.dp

@Composable
fun AppToolBar(
    backAction: () -> Unit,
    currentScreen: String,
    user: User? = null,
    scrollOffset: Float = 0f,
    searchStarted: State<Boolean>,
    searchAction: (input: String) -> Unit,
) {
    val input = remember {
        mutableStateOf("")
    }
    TopAppBar(
        title = {
            when (currentScreen) {
                ScreenNames.PROFILE -> {
                    ProfileCollapsedTopBar(
                        scrollOffset = scrollOffset,
                        user = user
                    )
                }

                ScreenNames.SEARCH -> {
                    SearchToolBar(
                        input = input,
                        searchAction = searchAction
                    )
                }

                else -> {
                    Row() {
                        Text(
                            text = currentScreen,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primaryVariant,
                        )
                        Image(
                            painter = painterResource(id = R.drawable.speer),
                            contentDescription = "App Logo"
                        )
                    }

                }

            }
        },
        backgroundColor = Color.White,
        elevation = if (searchStarted.value) 4.dp else 0.dp,
        navigationIcon = {
            if (currentScreen != ScreenNames.SEARCH) {
                IconButton(onClick = {
                    backAction()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go Back",
                        tint = Color.White,
                    )
                }
            }
        }
    )
}

@Composable
fun SearchToolBar(
    input: MutableState<String>,
    searchAction: (input: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        SearchTextField(
            hint = "Search User",
            fieldValue = input,
            action = searchAction,
        )
    }
}

@Composable
private fun ExpandedTopBar(
    input: MutableState<String>,
    action: (input: String) -> Unit,
    searchStarted: State<Boolean>,
) {

    val density = LocalDensity.current
    val heightInDp = animateDpAsState(
        targetValue = if (!searchStarted.value) with(density) { EXPANDED_TOP_BAR_HEIGHT } else COLLAPSED_TOP_BAR_HEIGHT,
        animationSpec = tween(
            durationMillis = 1000,
        )
    )
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(heightInDp.value),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (!searchStarted.value) {
                Image(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    painter = painterResource(id = R.drawable.speer),
                    contentDescription = "app",
                )
            }
            SearchToolBar(
                input = input,
                searchAction = action
            )
        }

    }
}

@Composable
private fun ProfileCollapsedTopBar(
    user: User? = null,
    scrollOffset: Float,
) {
    val imageSize by animateDpAsState(targetValue = max(72.dp, 128.dp * scrollOffset))
    val dynamicLines = max(3f, scrollOffset * 6).toInt()
    Row {
        AsyncImage(
            modifier = Modifier.size(imageSize),
            model = user?.avatar ?: "",
            contentDescription = "Avatar"
        )
        Column {
            Text(text = user?.name ?: "")
            Text(
                text = user?.username?.trim() ?: "",
                maxLines = 1
            )
            Text(
                text = user?.bio?.trim() ?: "",
                maxLines = dynamicLines
            )
        }
    }
}