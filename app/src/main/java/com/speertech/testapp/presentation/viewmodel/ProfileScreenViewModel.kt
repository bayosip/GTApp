package com.speertech.testapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.network.util.Resource
import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.presentation.usecases.GetFollowsUsecase
import com.speertech.testapp.presentation.usecases.GetUserUsecase
import com.speertech.testapp.presentation.view.screens.screen_states.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val getUserUsecase: GetUserUsecase,
    private val getFollowsUsecase: GetFollowsUsecase,
) : BaseViewModel() {

    private val state: MutableState<ProfileScreenState> = mutableStateOf(ProfileScreenState())
    val _state: State<ProfileScreenState> = state
    private val follows: MutableState<List<FollowModel>?> = mutableStateOf( null)
    private val userBackStack = mutableListOf<String>()

    private fun clearData() {
        follows.value = null
        state.value = _state.value.copy(follows = follows.value)
    }

    private fun addToBackStack(username: String) {
        if (!userBackStack.contains(username)) userBackStack.add(username)
    }

    fun isBackStackEmpty(): Boolean {
        userBackStack.removeLast()
        return userBackStack.isEmpty()
    }

    fun getUser(username: String) {
        clearData()
        addToBackStack(username)
        viewModelScope.launch(Dispatchers.IO) {
            getUserUsecase(username).collectLatest { result ->
                when (result.status) {
                    Resource.STATUS.SUCCESS -> {
                        withContext(Dispatchers.Main) {
                            state.value = _state.value.copy(
                                user = result.data,
                                isLoading = false,
                            )
                        }
                    }

                    Resource.STATUS.ERROR -> Log.e(
                        "TAG",
                        "getVideos: ${result.errorCode} - ${result.message}",
                    )

                    Resource.STATUS.LOADING -> {
                        withContext(Dispatchers.Main) {
                            state.value = _state.value.copy(isLoading = true)
                        }
                    }
                }

            }
        }
    }

    fun getFollows(follow_designation: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val username = _state.value.user?.username
            username?.let { name ->
                getFollowsUsecase(name, follow_designation).collectLatest { result ->
                    when (result.status) {
                        Resource.STATUS.SUCCESS -> {
                            withContext(Dispatchers.Main) {
                                result.data?.let { list ->
                                    clearData()
                                    follows.value =list
                                    state.value = _state.value.copy(
                                        follows = follows.value,
                                        isLoading = false,
                                    )
                                    Log.d("TAG", "Follows_xx: ${_state.value.follows?.get(0)}")
                                }
                            }
                        }

                        Resource.STATUS.ERROR -> Log.e(
                            "TAG",
                            "getVideos: ${result.errorCode} - ${result.message}",
                        )

                        Resource.STATUS.LOADING -> {
                            withContext(Dispatchers.Main) {
                                state.value = _state.value.copy(isLoading = true)
                            }
                        }
                    }
                }
            }
        }
    }

    fun getLastUser() {
        val name = userBackStack.last()
        getUser(name)
    }

}