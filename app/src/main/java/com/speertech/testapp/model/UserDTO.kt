package com.speertech.testapp.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("login")
    val username: String,
    @SerializedName("avatar_url")
    val avatar: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("following")
    val following: Int,
)