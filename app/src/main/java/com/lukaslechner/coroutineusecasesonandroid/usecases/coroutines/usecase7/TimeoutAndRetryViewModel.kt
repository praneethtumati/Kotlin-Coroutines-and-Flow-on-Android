package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeout = 1000L


        // run api.getAndroidVersionFeatures(27) and api.getAndroidVersionFeatures(28) in parallel

        val androidOreoVersion = viewModelScope.async {
            retryWithTimeOut(
                numberOfRetries,timeout
            ){
                api.getAndroidVersionFeatures(27)
            }
        }
        val androidPieVersion = viewModelScope.async {
            retryWithTimeOut(
                numberOfRetries,timeout
            ){
                api.getAndroidVersionFeatures(27)
            }
        }

        viewModelScope.launch {
            try {
                uiState.value = UiState.Success(listOf(androidPieVersion,androidOreoVersion).awaitAll())
            }catch (e:Exception){
                uiState.value  = UiState.Error(e.localizedMessage.ifEmpty { "Something went wrong!." })
            }
        }

    }

    private suspend fun <T> retryWithTimeOut(numberOfAttempts: Int, timeOut:Long, block:suspend ()->T):T = retry(numberOfAttempts){
        withTimeout(timeOut){
            block()
        }
    }


    private suspend fun <T> retry(numberOfAttempts:Int,delayBetweenRetries:Long = 100, block:suspend()->T):T{
        repeat(numberOfAttempts){
            try {
                return block()
            }
            catch (e:Exception){
                Timber.e(e)
            }
            delay(delayBetweenRetries)
        }
        return block()
    }
}