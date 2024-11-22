package org.mavriksc.messin.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    coroutineScope {
        launch {
            delay(1000)
            println("World!")
        }
        print("Hello ")
        //a delay to wait for the coroutine.
        delay(2000)
    }
}
