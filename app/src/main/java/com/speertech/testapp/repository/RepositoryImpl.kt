package com.speertech.testapp.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.model.GHSearchResults
import com.speertech.testapp.model.User
import com.speertech.testapp.network.ApiService
import com.speertech.testapp.network.util.BaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val service: ApiService
) : Repository, BaseDataSource() {

    private suspend fun getSearchResultsOf(username: String): Resource<GHSearchResults?> = getResult {
        service.searchForUser(user = username)
    }

    private suspend fun getUserWith(username: String): Resource<User?> = getResult {
        service.getUser(user = username)
    }

    private suspend fun getFollowsOf(username: String, follow_designation: Int): Resource<List<FollowModel>?> =
        getResult {
        when(follow_designation){
            FollowsDesignation.FOLLOWERS -> service.getFollowers(username)
            else -> service.getFollowing(username)
        }
    }


    override suspend fun getFollows(
        user: String,
        follow_designation: Int
    ): Flow<Resource<List<FollowModel>?>> = flow {
        val response = getFollowsOf(username = user, follow_designation)
        when (response.status) {
            Resource.STATUS.SUCCESS -> {
                val result = response.data
                if (result != null) {
                    emit(Resource.success(result))
                }
            }
            Resource.STATUS.ERROR -> {
                Log.d("TAG", "getSearchResults: request error")
                emit(Resource.error(message = "Request Error..."))
            }
            Resource.STATUS.LOADING -> emit(Resource.loading())
        }
    }

    override suspend fun getUser(user: String): Flow<Resource<User?>> = flow{
        emit(Resource.loading())
        val response = getUserWith(username = user)
        when (response.status) {
            Resource.STATUS.SUCCESS -> {
                val result = response.data
                if (result != null) {
                    emit(Resource.success(result))
                }
            }
            Resource.STATUS.ERROR -> {
                Log.d("TAG", "getSearchResults: request error")
                emit(Resource.error(message = "Request Error..."))
            }
            Resource.STATUS.LOADING -> emit(Resource.loading())
        }
    }

    suspend fun getSearchResults(username: String):
            Flow<Resource<GHSearchResults>> = flow {
        emit(Resource.loading())
        val response = getSearchResultsOf(username)
        when (response.status) {
            Resource.STATUS.SUCCESS -> {
                val result = response.data
                if (result != null) {
                    emit(Resource.success(result))
                }
            }
            Resource.STATUS.ERROR -> {
                Log.d("TAG", "getSearchResults: request error")
                emit(Resource.error(message = "Request Error..."))
            }
            Resource.STATUS.LOADING -> emit(Resource.loading())
        }
    }

    override suspend fun getUserSearchResult(username: String):
            Flow<PagingData<FollowModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
            ),
            pagingSourceFactory = {
                SearchPagingSource(this@RepositoryImpl, username)
            }
        ).flow
    }
}