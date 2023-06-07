package com.speertech.testapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.speertech.testapp.presentation.view.screens.ScreenNames
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }

    @Composable
    fun AppContent(){
        val listState = rememberLazyListState()
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {

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

                    }
                }
            }

        }
    }
}