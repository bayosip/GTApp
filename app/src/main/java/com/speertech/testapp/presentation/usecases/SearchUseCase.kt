package com.speertech.testapp.presentation.usecases

import androidx.paging.PagingData
import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.repository.RepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchUseCase @Inject constructor(
private val repository: RepositoryImpl
) {
    suspend operator fun invoke(username:String): Flow<PagingData<FollowModel>> =
        repository.getUserSearchResult(username)
}