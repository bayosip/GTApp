package com.speertech.testapp.presentation.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceUseCase {

    fun <T> debounce(
        delayMillis: Long = 800L,
        scope: CoroutineScope,
        action: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            if (debounceJob == null) {
                debounceJob = scope.launch {
                    action(param)
                    delay(delayMillis)
                    debounceJob = null
                }
            }
        }
    }
}