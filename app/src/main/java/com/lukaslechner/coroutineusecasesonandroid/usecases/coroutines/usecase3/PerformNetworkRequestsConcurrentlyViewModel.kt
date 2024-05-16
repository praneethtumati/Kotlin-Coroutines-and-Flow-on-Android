package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                var oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                var pieFeatures = mockApi.getAndroidVersionFeatures(28)
                var android10Features = mockApi.getAndroidVersionFeatures(29)
                uiState.value = UiState.Success(listOf(oreoFeatures,pieFeatures,android10Features))

            }
            catch (e:Exception){
                uiState.value = UiState.Error(e.message.toString().ifEmpty { "Something went wrong" })
            }

        }

    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading
        val oreoFeatures = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(27)
        }
        val pieFeatures = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(28)
        }
        val android10Features = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(29)
        }
        try {
            viewModelScope.launch {
                uiState.value = UiState.Success(awaitAll(oreoFeatures,pieFeatures,android10Features))
            }
        }
        catch (e:Exception){
            uiState.value = UiState.Error(e.message.toString().ifEmpty { "Something went wrong" })
        }
    }
}