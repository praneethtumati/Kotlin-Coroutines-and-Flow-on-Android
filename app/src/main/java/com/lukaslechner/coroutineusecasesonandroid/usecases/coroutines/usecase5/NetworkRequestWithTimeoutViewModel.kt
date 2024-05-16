package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeout: Long) {
        uiState.value = UiState.Loading
//       usingTimeOut(timeout)
        usingTimeOutWithNull(timeout)
    }

    fun usingTimeOut(timeout: Long){
        viewModelScope.launch {
            try {
                val recentAndroidVersion = withTimeout(timeout){
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(recentAndroidVersion)
            }
            catch (timeOutException:TimeoutCancellationException){
                uiState.value = UiState.Error("Network Timed Out.")
                Timber.e("${timeOutException.message}")
            }
            catch (e:Exception){
                uiState.value = UiState.Error("Something went wrong at the server side.")
                Timber.e("${e.message}")
            }

        }
    }

    fun usingTimeOutWithNull(timeout: Long){
        viewModelScope.launch {
            try {
                val recentAndroidVersion = withTimeoutOrNull(timeout){
                    api.getRecentAndroidVersions()
                }
                if(recentAndroidVersion != null){
                    uiState.value = UiState.Success(recentAndroidVersion)
                }
                else{
                    uiState.value = UiState.Error("Network Timed Out.")
                }
            }
            catch (e:Exception){
                uiState.value = UiState.Error("Something went wrong at the server side.")
                Timber.e("${e.message}")
            }

        }
    }

}