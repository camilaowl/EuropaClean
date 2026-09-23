package com.ca.europaclean

class Greeting(
    private val platform: Platform,
) {
    fun greet(): String {
        return sayHello(platform.name)
    }
}
