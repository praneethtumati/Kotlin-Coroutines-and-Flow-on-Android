package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinesbuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val startTime=  System.currentTimeMillis()
    val deferred1 = async(start = CoroutineStart.LAZY) {
        val result1 = networkCall(1)
        println("result received: ${result1} after ${elapsedMillis(startTime)}ms")
        result1
    }

    val deferred2 = async {
        val result2 = networkCall(2)
        println("result received: ${result2} after ${elapsedMillis(startTime)}ms")
        result2
    }
//    deferred1.join()
//    deferred1.cancel()
    deferred1.start()
    val resultList = arrayListOf(deferred1.await(),deferred2.await())
    println("result list: ${resultList} after ${elapsedMillis(startTime)}ms")
}
/*fun main() = runBlocking<Unit> {
    val startTime=  System.currentTimeMillis()
    val deferred1 = async {
        val result1 = networkCall(1)
        println("result received: ${result1} after ${elapsedMillis(startTime)}ms")
        result1
    }

    val deferred2 = async {
        val result2 = networkCall(2)
        println("result received: ${result2} after ${elapsedMillis(startTime)}ms")
        result2
    }
    val resultList = arrayListOf(deferred1.await(),deferred2.await())
    println("result list: ${resultList} after ${elapsedMillis(startTime)}ms")
}*/

fun elapsedMillis(startTime:Long) = System.currentTimeMillis() - startTime

suspend fun networkCall(number:Int):String{
    delay(500)
    return "Result: $number"
}