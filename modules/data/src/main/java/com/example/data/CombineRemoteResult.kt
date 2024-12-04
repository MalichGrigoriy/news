package com.example.data

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T : Any> Flow<RequestResult<*>>.combineResultData(
    other: Flow<T>
): Flow<RequestResult<T>> = flow {
    val firstLatest = MutableStateFlow<RequestResult<*>?>(null)
    val secondLatest = MutableStateFlow<T?>(null)
    var waitForSuccess: Boolean = false

    coroutineScope {
        launch {
            this@combineResultData.collect { value ->
                firstLatest.value = value
                secondLatest.value?.let { secondLatestValue ->
                    when (value) {
                        is RequestResult.InProgress<*> -> emit(RequestResult.InProgress(secondLatestValue))
                        is RequestResult.Error<*> -> emit(RequestResult.Error(secondLatestValue))
                        is RequestResult.Success<*> -> {
                            if (waitForSuccess) {
                                waitForSuccess = false
                                emit(RequestResult.Success(secondLatestValue))
                            }
                        }

                        is RequestResult.Ignore -> TODO()
                    }
                }
            }
        }
        launch {
            other.collect { value ->
                firstLatest.value?.let { firstLatestValue ->
                    when (firstLatestValue) {
                        is RequestResult.InProgress<*> -> {
                            if (secondLatest.value == null) {
                                emit(RequestResult.InProgress(value))
                            } else {
                                waitForSuccess = true
                            }
                        }

                        is RequestResult.Error<*> -> emit(RequestResult.Error(value))
                        is RequestResult.Success<*> -> emit(RequestResult.Success(value))
                        is RequestResult.Ignore -> TODO()
                    }
                    secondLatest.value = value
                }
            }
        }
    }
}
