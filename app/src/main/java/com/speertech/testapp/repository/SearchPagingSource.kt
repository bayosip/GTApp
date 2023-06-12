package com.speertech.testapp.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.model.GHSearchResults
import kotlinx.coroutines.flow.collectLatest
import okio.IOException

class SearchPagingSource(
    private val repository: RepositoryImpl,
    private val username: String,
) : PagingSource<Int, FollowModel>() {
    override fun getRefreshKey(state: PagingState<Int, FollowModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FollowModel> {
        return try {
            val page = params.key ?: 1
            var result: GHSearchResults? = null
            repository.getSearchResults(username).collectLatest {
                Log.d("TAG", "load: ${it.status}")
                when (it.status) {
                    Resource.STATUS.SUCCESS -> {
                        result = it.data
                        Log.d("TAG", "load: is successful - total: ${it.data?.totalCount}")
                    }
                    Resource.STATUS.LOADING -> {}
                    Resource.STATUS.ERROR -> {
                        Log.d("TAG", "load: ${it.status}")
                        throw IOException("request unsuccessful")
                    }
                }
            }
            LoadResult.Page(
                data = result?.users ?: emptyList<FollowModel>(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (result != null && (result?.users?.isEmpty() == true)) null else page.plus(
                    1
                ),
            )
        } catch (e: Exception) {
            Log.e("TAG", "load: ${e.message}", e)
            LoadResult.Error(e)
        }
    }

}