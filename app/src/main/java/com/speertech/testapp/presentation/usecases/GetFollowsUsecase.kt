package com.speertech.testapp.presentation.usecases

import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFollowsUsecase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        username: String,
        follow_designation: Int
    ): Flow<Resource<List<FollowModel>?>> =
        repository.getFollows(username, follow_designation)
}