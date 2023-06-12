package com.speertech.testapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.speertech.testapp.presentation.view.screens.ProfileScreen
import com.speertech.testapp.presentation.view.screens.ScreenNames
import com.speertech.testapp.presentation.view.screens.SearchScreen
import com.speertech.testapp.presentation.view.toolbar.AppToolBar
import com.speertech.testapp.presentation.viewmodel.ProfileScreenViewModel
import com.speertech.testapp.presentation.viewmodel.SearchScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val searchViewModel: SearchScreenViewModel by viewModels()
    private val profileScreenViewModel: ProfileScreenViewModel by viewModels()
    private val KEY_USER = "user_key"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }

    @Composable
    fun AppContent() {
        //val listState = rememberLazyListState()
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val hasSearchStarted = rememberSaveable {
            mutableStateOf(false)
        }
        val scrollOffset = rememberSaveable {
            mutableStateOf(0f)
        }
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                AppToolBar(
                    backAction = {
                        hasSearchStarted.value = false
                        if (profileScreenViewModel.isBackStackEmpty())
                            navController.popBackStack()
                        else{
                            profileScreenViewModel.getLastUser()
                        }
                    },
                    currentScreen = navController
                        .currentBackStackEntryAsState()
                        .value?.destination?.route ?: ScreenNames.SEARCH,
                )
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it.calculateBottomPadding())
            ) {
                NavHost(
                    navController = navController,
                    startDestination = ScreenNames.SEARCH
                ) {
                    composable(ScreenNames.SEARCH) {
                        SearchScreen(
                            hasSearchStarted = hasSearchStarted,
                            searchScreenState = searchViewModel._state,
                            navigateToUserScreen = { user ->
                                searchViewModel.clearData()
                                navigateWithArguments(
                                    argument = user,
                                    screen = ScreenNames.PROFILE,
                                    navController = navController,
                                )
                            }
                        ) { typedInput ->
                            searchViewModel.getSearchForUsername(typedInput)
                            hasSearchStarted.value = true
                        }
                    }
                    composable(ScreenNames.PROFILE.plus("/{$KEY_USER}")) { backStackEntry ->
                        ProfileScreen(
                            username = backStackEntry.arguments?.getString(KEY_USER, "") ?: "",
                            screenState = profileScreenViewModel._state,
                            launchQuery = { user ->
                                profileScreenViewModel.getUser(user)
                            },
                            scrollOffset = scrollOffset,
                            checkFollowsAction = { follow_designation ->
                                profileScreenViewModel.getFollows(follow_designation)
                            },
                        )
                    }
                }
            }

        }
    }

    private fun navigateWithArguments(
        argument: String? = null,
        screen: String,
        navController: NavHostController
    ) {
        var route = screen
        // If argument is supplied, navigate using that argument
        argument?.let {
            route = screen.plus("/$it")
        }
        navController.navigate(route)
    }
}