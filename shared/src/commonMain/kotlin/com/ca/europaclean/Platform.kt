package com.ca.europaclean

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform