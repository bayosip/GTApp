package com.speertech.testapp.presentation.usecases

import androidx.paging.PagingData
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchUseCase @Inject constructor(
    private val repository: Repository,
) {
    suspend operator fun invoke(username:String): Flow<PagingData<FollowModel>> =
        repository.getUserSearchResult(username)
}