package com.speertech.testapp.model

import com.google.gson.annotations.SerializedName

data class GHSearchResults(
    @SerializedName("total_count")
    val totalCount: Long,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val users: List<FollowModel>?,
)
