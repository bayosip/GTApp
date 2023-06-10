package com.speertech.testapp.presentation.view.ui_components

import android.view.KeyEvent
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.silverorange.videoplayer.R
import com.speertech.testapp.GTApp
import com.speertech.testapp.model.FollowModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchButton(
    action: () -> Unit,
    text: String,
    modifier: Modifier,
) {
    Button(
        onClick = { action() },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Green,
            contentColor = Color.White
        ),
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
        )
    }
}

@Composable
fun SearchTextField(
    hint: String,
    fieldValue: MutableState<String>,
    action: (txt: String) -> Unit,
) {
    val lightBlue = Color(0xffd8e6ff)
    val blue = Color(0xff1aa7ec)

    TextField(
        maxLines = 1,
        value = fieldValue.value ?: "",
        onValueChange = {
            fieldValue.value = it
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                action(fieldValue.value)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    //focusRequester.requestFocus()
                    action(fieldValue.value)
                    true
                }
                false
            },
        label = {
            Text(
                text = hint,
                fontSize = 8.sp,
            )
        },
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            if (fieldValue.value.isNotEmpty()) {
                IconButton(onClick = { fieldValue.value = "" }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = blue
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Outlined.PersonSearch,
                    contentDescription = "Search",
                    tint = blue,
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = lightBlue,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
    )
}

@Composable
fun InfoText(
    modifier: Modifier,
    text: String = ""
) {
    Text(
        text = text,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color.DarkGray,
        modifier = modifier
    )
}

@Composable
fun SearchResultListItem(
    item: FollowModel,
    onItemClicked: (username: String) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                .clickable { onItemClicked(item.username) }
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    modifier = Modifier
                        .size(88.dp)
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                    model = item.avatar,
                    contentDescription = "avatar thumbnail",
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = item.username,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1
                )
            }
        }
        Divider(modifier = Modifier.height(3.dp))
    }

}

fun Modifier.onClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {

    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = {
            GTApp.debounceClicks {
                onClick.invoke()
            }
        },
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }
    )
}