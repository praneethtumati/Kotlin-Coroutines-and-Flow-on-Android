package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlin.concurrent.thread

fun main(){
    println("main function starts")
    threadRoutine(1,500)
    threadRoutine(2,300)
    Thread.sleep(1000)
    println("main function ends")
}

fun threadRoutine(number:Int, delay:Long){
    thread {
        println("Routine $number starts work")
        Thread.sleep(delay)
        println("Routine $number has finished.")
    }
}