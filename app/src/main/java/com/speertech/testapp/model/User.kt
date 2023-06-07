package com.speertech.testapp.model

import com.google.gson.annotations.SerializedName

data class User (

    val username: String,
    @SerializedName("avatar_url")
    val avatar: String,
    @SerializedName("name")
    val name: String,
    val description: String,
    @SerializedName("followers")
    val followers: String,
    @SerializedName("following")
    val following: String,
)