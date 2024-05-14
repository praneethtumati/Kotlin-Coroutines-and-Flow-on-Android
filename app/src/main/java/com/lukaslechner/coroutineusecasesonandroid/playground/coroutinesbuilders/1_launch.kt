package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinesbuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

 /** main function as an expression. **/
//fun main() = runBlocking {
//    launch{
//        delay(500)
//        println("Printed from withing coroutine")
//    }
//}

// 2 coroutines are launched run-blocking (coroutine 1) and launch (coroutine 2), and run blocking cannot access result of network request.

// here end of run blocking gets printed first as second coroutine waits till network request is completed.
/*fun main() = runBlocking {
    val job = launch {
        networkRequest()//here we don't have the possibility to access result string value outside launch
        println("Result Received from network request")
    }
    println("End of Run Blocking")
}*/

suspend fun networkRequest():String{
    delay(500)
    return "Result"
}

/** basic understanding for launch and runblocking with the below example **/
//fun main(){
//    runBlocking {
//        launch{//run blocking lambda receiver itself is a coroutine scope.
//            delay(500)
//            println("Printed from within coroutine")
//        }
//    }
//    GlobalScope.launch{
//        delay(500)
//        println("Printed from within coroutine")
//    }
//    Thread.sleep(1000) //now main thread waits for coroutine delay to complete here launch is not a blocking call.
//    println("Main ends here.")
//}



/*
fun main() = runBlocking {
    val job = launch {
        networkRequest()//here we don't have the possibility to access result string value outside launch
        println("Result Received from network request")
    }
    job.join()//makes 1 st coroutine to wait till the inner coroutines gets completed.
    println("End of Run Blocking")
}*/


fun main() = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        networkRequest()//here we don't have the possibility to access result string value outside launch
        println("Result Received from network request")
    }
    job.start()//lazy start the second coroutine.
    println("End of Run Blocking")
}

