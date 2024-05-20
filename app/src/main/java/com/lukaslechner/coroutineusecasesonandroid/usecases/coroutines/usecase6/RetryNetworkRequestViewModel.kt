package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import android.text.style.TtsSpan.TimeBuilder
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        viewModelScope.launch {
            val numberOfRetries = 2
            try {
                /*repeat(numberOfRetries){
                    try {
                        loadRecentAndroidVersions()
                        return@launch
                    }
                    catch (e:Exception){
                        Timber.e("Failed Network Request")
                    }
                }
                loadRecentAndroidVersions()*/
                retry(numberOfRetries){
                    loadRecentAndroidVersions()
                }
            }
            catch (timeOutException: TimeoutCancellationException){
                uiState.value = UiState.Error("Network Timed Out.")
                Timber.e("${timeOutException.message}")
            }
            catch (e:Exception){
                uiState.value = UiState.Error("Something went wrong at the server side.")
                Timber.e("${e.message}")
            }

        }
    }

    private suspend fun <T> retry(numberOfAttempts:Int,initialDelayInMillis:Long = 100, maxDelayInMillis:Long = 1000, factor:Double = 2.0, block:suspend()->T):T{
        var currentDelayInMillis = initialDelayInMillis
        repeat(numberOfAttempts){
            try {
                return block()
            }
            catch (e:Exception){
                Timber.e(e)
            }
            delay(currentDelayInMillis)
            currentDelayInMillis = (currentDelayInMillis*factor).toLong().coerceAtMost(maxDelayInMillis)
        }
        return block()
    }

    suspend  fun loadRecentAndroidVersions(){
        val recentAndroidVersion = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersion)
    }

}