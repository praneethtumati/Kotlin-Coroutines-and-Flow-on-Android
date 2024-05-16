package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersion = mockApi.getRecentAndroidVersions()
                uiState.value = UiState.Success(recentAndroidVersion)
            }
            catch (e:Exception){
                uiState.value = UiState.Error("Something went wrong at the server side.")
                Timber.e("${e.message}")
            }

        }
    }
}