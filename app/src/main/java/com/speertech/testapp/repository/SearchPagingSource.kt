package com.speertech.testapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.model.SearchResults
import com.speertech.testapp.model.User
import kotlinx.coroutines.flow.collectLatest

class SearchPagingSource (
    private val repository: RepositoryImpl,
    private val username:String,
): PagingSource<Int, FollowModel>() {
    override fun getRefreshKey(state: PagingState<Int, FollowModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FollowModel> {
        return try {
            val page = params.key ?: 1
            var result:SearchResults? = null
            repository.getSearchResults( username).collectLatest {
                result = it.data
            }
            LoadResult.Page(
                data = result?.users?: emptyList<FollowModel>(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (result?.users?.isEmpty() == true) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}