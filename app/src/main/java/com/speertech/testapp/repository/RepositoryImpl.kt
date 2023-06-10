package com.speertech.testapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.model.SearchResults
import com.speertech.testapp.model.User
import com.speertech.testapp.network.ApiService
import com.speertech.testapp.network.util.BaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val service: ApiService) : Repository, BaseDataSource() {

    private suspend fun getSearchResultsOf(username: String): Resource<SearchResults?> = getResult {
        service.searchForUser(user = username)
    }


    override suspend fun getFollows(
        user: String,
        follow_designation: Int
    ): Flow<Resource<List<FollowModel>?>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(user: String): Flow<Resource<User?>> {
        TODO("Not yet implemented")
    }

    suspend fun getSearchResults(username: String): Flow<Resource<SearchResults>> = flow{
        val response = getSearchResultsOf(username)
        when (response.status) {
            Resource.STATUS.SUCCESS -> {
                val result = response.data
                if (result != null) {
                    emit(Resource.success(result))
                }
            }
            Resource.STATUS.ERROR -> emit(Resource.error(message = "Request Error..."))
            Resource.STATUS.LOADING -> emit(Resource.loading())
        }
    }

    override suspend fun getUserSearchResult(username: String):
            Flow<PagingData<FollowModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
            ),
            pagingSourceFactory = {
                SearchPagingSource(this@RepositoryImpl, username)
            }
        ).flow
    }
}