package com.speertech.testapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.speertech.testapp.presentation.view.screens.ScreenNames
import com.speertech.testapp.presentation.view.screens.SearchScreen
import com.speertech.testapp.presentation.view.toolbar.AppToolBar
import com.speertech.testapp.presentation.viewmodel.SearchScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val searchViewModel: SearchScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }

    @Composable
    fun AppContent() {
        val listState = rememberLazyListState()
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val hasSearchStarted = rememberSaveable {
            mutableStateOf(false)
        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                AppToolBar(
                    backAction = { navController.popBackStack() },
                    currentScreen = navController
                        .currentBackStackEntryAsState()
                        .value?.destination?.route ?: ScreenNames.SEARCH,
                    searchStarted = hasSearchStarted
                ) { typedInput ->
                    searchViewModel.getSearchForUsername(typedInput)
                    hasSearchStarted.value = true
                }
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
                            screenState = searchViewModel._state
                        )
                    }
                }
            }

        }
    }
}