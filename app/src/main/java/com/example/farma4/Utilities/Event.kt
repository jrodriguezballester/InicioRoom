package com.example.farma4.Utilities

import android.util.Log

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            Log.i("MyTAG", "retorna null ")
            null
        } else {
            hasBeenHandled = true
            Log.i("MyTAG", "retorna content ${content} ")
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}