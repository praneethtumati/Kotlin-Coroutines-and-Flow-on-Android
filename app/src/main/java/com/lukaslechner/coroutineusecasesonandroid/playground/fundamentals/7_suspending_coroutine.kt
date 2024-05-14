package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("main starts")
    joinAll(async {
        suspendingCoroutine(1,500)
    },async {
        suspendingCoroutine(2,300)
    },
        async {
            repeat(5){
                println("Other Task running on ${Thread.currentThread().name}")
                delay(100)
            }
        })
    println("main ends")
}

suspend fun suspendingCoroutine(number: Int, delay:Long){
    println("Coroutines ${number} starts work on: ${Thread.currentThread().name}")
    delay(delay)
    println("Coroutines ${number} ends work.")
}