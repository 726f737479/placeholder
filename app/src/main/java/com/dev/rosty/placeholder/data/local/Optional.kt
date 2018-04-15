package com.dev.rosty.placeholder.data.local

class Optional<out T>(val value: T? = null) {

    fun hasValue(): Boolean {
        return value != null
    }
}