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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.silverorange.videoplayer.R
import com.speertech.testapp.model.User
import com.speertech.testapp.model.inWords
import com.speertech.testapp.presentation.view.screens.ScreenNames
import com.speertech.testapp.presentation.view.ui_components.SearchTextField
import com.speertech.testapp.presentation.view.ui_components.onClick
import com.speertech.testapp.repository.FollowsDesignation
import java.lang.Float.max

val COLLAPSED_TOP_BAR_HEIGHT = 90.dp
val EXPANDED_TOP_BAR_HEIGHT = 550.dp

@Composable
fun AppToolBar(
    backAction: (screen:String) -> Unit,
    currentScreen: String,
) {
    if (currentScreen != ScreenNames.SEARCH) {
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    modifier = Modifier.fillMaxWidth(),

                )
            },
            backgroundColor = Color.White,
            navigationIcon = {
                IconButton(onClick = {
                    backAction(currentScreen)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go Back",
                        tint = Color.Black,
                    )
                }
            }
        )
    }
}

@Composable
fun SearchToolBar(
    input: MutableState<String>,
    searchAction: (input: String) -> Unit,
    hasSearchStarted: State<Boolean>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasSearchStarted.value) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(id = R.drawable.speer),
                contentDescription = "app",
            )
        }
        SearchTextField(
            hint = "Search User",
            fieldValue = input,
            action = searchAction,
        )
    }
}

@Composable
fun ExpandedTopBar(
    input: MutableState<String>,
    action: (input: String) -> Unit,
    searchStarted: State<Boolean>,
) {

    val density = LocalDensity.current
    val heightInDp = animateDpAsState(
        targetValue = if (!searchStarted.value) with(density) { EXPANDED_TOP_BAR_HEIGHT } else 0.dp,
        animationSpec = tween(
            durationMillis = 500,
        )
    )

    if (searchStarted.value && heightInDp.value == 0.dp) {
        SearchToolBar(
            input = input,
            searchAction = action,
            hasSearchStarted = searchStarted,
        )
    }

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
                searchAction = action,
                hasSearchStarted = searchStarted,
            )
        }
    }
}

@Composable
fun ProfileCollapsedTopBar(
    user: User? = null,
    scrollOffset: State<Float>,
) {
    val imageSize by animateDpAsState(
        targetValue = max(72.dp, 128.dp * scrollOffset.value)
    )
    val dynamicLines = max(3f, scrollOffset.value * 6).toInt()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AsyncImage(
            modifier = Modifier
                .size(imageSize)
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(12.dp)),
            model = user?.avatar ?: "",
            contentDescription = "Avatar"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = user?.name ?: "",
                fontSize = 24.sp
            )
            Text(
                text = user?.username?.trim() ?: "",
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
                maxLines = 1
            )
            Text(
                text = user?.bio?.trim() ?: "",
                maxLines = dynamicLines
            )
        }
    }
}