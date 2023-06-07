package com.speertech.testapp.presentation.view.toolbar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


@Composable
fun AppToolBar(
    backAction: () -> Unit,
    currentScreen: String,
    user: User? = null,
    scrollOffset: Float = 0f,
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

                ScreenNames.RESULTS -> {
                    ResultToolBar(input = input)
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
        backgroundColor = Color.Green,
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
fun ResultToolBar(
    input: MutableState<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
    ) {
        SearchTextField(
            hint = "Search User",
            modifier = Modifier.fillMaxWidth(),
            fieldValue = input
        )
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
                text = user?.description?.trim() ?: "",
                maxLines = dynamicLines
            )
        }
    }
}