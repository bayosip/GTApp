package com.speertech.testapp.presentation.view.screens.screen_states

import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.model.User

data class ProfileScreenState(
    val follows: List<FollowModel>? = null,
    val user: User? = null,
    val isLoading: Boolean = false,
)
