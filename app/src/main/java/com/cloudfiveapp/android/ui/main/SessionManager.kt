package com.cloudfiveapp.android.ui.main

object SessionManager {

    private val sessionObservers = mutableListOf<SessionObserver>()

    var loggedIn: Boolean = false
        set(value) {
            sessionObservers.forEach {
                it.onChanged(value)
            }
            field = value
        }

    fun addSessionObserver(observer: SessionObserver) {
        sessionObservers.add(observer)
        observer.onChanged(loggedIn)
    }

    fun removeSessionObserver(observer: SessionObserver) {
        sessionObservers.remove(observer)
    }
}

interface SessionObserver {
    fun onChanged(loggedIn: Boolean)
}
