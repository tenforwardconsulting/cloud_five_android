package com.cloudfiveapp.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object SessionManager {

    private val session = MutableLiveData<Session>()

    fun getSession(): LiveData<Session> {
        return session
    }

    fun logIn(authToken: String) {
        session.postValue(Authenticated(authToken))
    }

    fun logOut() {
        session.postValue(Unauthenticated)
    }
}

sealed class Session

object Unauthenticated : Session()

data class Authenticated(val authToken: String) : Session()

