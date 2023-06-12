package com.speertech.testapp.presentation.usecases

import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.User
import com.speertech.testapp.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUsecase @Inject constructor(
    private val repository: Repository,
) {
    suspend operator fun invoke(username: String): Flow<Resource<User?>> =
        repository.getUser(user = username)
}