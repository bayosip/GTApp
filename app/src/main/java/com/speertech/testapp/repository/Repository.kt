package com.speertech.testapp.repository

import androidx.paging.PagingData
import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.model.SearchResults
import com.speertech.testapp.model.User
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getFollows(user: String, follow_designation:Int): Flow<Resource<List<FollowModel>?>>
    suspend fun getUser(user: String): Flow<Resource<User?>>
    suspend fun getUserSearchResult(username: String): Flow<PagingData<FollowModel>>
}