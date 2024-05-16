package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class VariableAmountOfNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                val versionFeatures = recentAndroidVersions.map {
                    mockApi.getAndroidVersionFeatures(it.apiLevel)
                }
                uiState.value = UiState.Success(versionFeatures)
            }
            catch (e:Exception){
                uiState.value = UiState.Error(e.message.toString().ifEmpty { "Something went wrong" })
            }

        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                val deferredList= recentAndroidVersions.map {
                    async { mockApi.getAndroidVersionFeatures(it.apiLevel) }
                }.awaitAll()
                uiState.value = UiState.Success(deferredList)
            }
            catch (e:Exception){
                uiState.value = UiState.Error(e.message.toString().ifEmpty { "Something went wrong" })
            }
        }
    }
}