package com.speertech.testapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltAndroidApp
class GTApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }

    @OptIn(DelicateCoroutinesApi::class, FlowPreview::class)
    companion object {
        private val debounceState = MutableStateFlow { }

        init {
            GlobalScope.launch(Dispatchers.Main) {
                // IMPORTANT: Make sure to import kotlinx.coroutines.flow.collect
                debounceState
                    .debounce(500)
                    .collect { onClick ->
                        onClick.invoke()
                    }
            }
        }

        fun debounceClicks(onClick: () -> Unit) {
            debounceState.value = onClick
        }
    }
}