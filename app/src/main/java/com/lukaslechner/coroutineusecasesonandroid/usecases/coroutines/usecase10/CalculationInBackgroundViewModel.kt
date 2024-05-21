package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.launch
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            var result = BigInteger.ONE
            val calculationTime = measureTimeMillis {
                result = factorialOf(factorialOf)
            }
            var resultString = ""
            val conversionTime = measureTimeMillis {
                resultString = result.toString()
            }
            uiState.value = UiState.Success(resultString,calculationTime,conversionTime)
        }
    }

    private fun factorialOf(number:Int):BigInteger{
        var result = BigInteger.ONE
        for(i in 1..number){
            result = result.multiply(BigInteger.valueOf(i.toLong()))
        }
        return result
    }
}