package com.cloudfiveapp.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SessionViewModel : ViewModel() {

    private val loggedIn = MutableLiveData<Boolean>()

    private val sessionObserver = object : SessionObserver {
        override fun onChanged(loggedIn: Boolean) {
            this@SessionViewModel.loggedIn.postValue(loggedIn)
        }
    }

    init {
        SessionManager.addSessionObserver(sessionObserver)
    }

    fun getSession(): LiveData<Boolean> {
        return loggedIn
    }

    override fun onCleared() {
        super.onCleared()
        SessionManager.removeSessionObserver(sessionObserver)
    }
}
