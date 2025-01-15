package org.akshar.camera

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform