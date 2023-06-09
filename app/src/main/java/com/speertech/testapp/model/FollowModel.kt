package com.speertech.testapp.model

import com.google.gson.annotations.SerializedName

data class FollowModel(
    @SerializedName("login")
    val username: String,
    @SerializedName("avatar_url")
    val avatar: String,
)
