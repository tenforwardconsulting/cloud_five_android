package com.cloudfiveapp.android.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.injection.Injector
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.util.CloudAnimator
import com.cloudfiveapp.android.util.extensions.get
import com.cloudfiveapp.android.util.extensions.showKeyboard
import com.cloudfiveapp.android.util.extensions.toast
import com.cloudfiveapp.android.util.extensions.toastNetworkError
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private val viewModelFactory = Injector.get().loginViewModelFactory()

    private val viewModel by lazy {
        viewModelFactory.get(this, LoginViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        lifecycle.addObserver(CloudAnimator(loginParentView, layoutInflater))

        bindToViewModel()
    }

    private fun bindToViewModel() {
        viewModel.loginViewState.observe(this, Observer { outcome ->
            Timber.d("$outcome")
            when (outcome) {
                is Outcome.Loading -> {
                    disableInputs()
                    loginEmailTextInputLayout.error = null
                    loginPasswordTextInputLayout.error = null
                }
                is Outcome.Success -> {
                    toast("Login success!")
                    enableInputs()
                    finish()
                }
                is Outcome.Error -> {
                    if (!outcome.message.isNullOrEmpty()) {
                        loginEmailTextInputLayout.error = outcome.message
                        loginPasswordTextInputLayout.error = outcome.message
                    } else {
                        toastNetworkError(outcome.error?.message)
                    }
                    enableInputs()
                    loginEmailInput.showKeyboard()
                }
            }
        })

        loginButton.setOnClickListener { attemptLogin() }

        loginPasswordInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    attemptLogin()
                    true
                }
                else -> false
            }
        }
    }

    private fun enableInputs() {
        loginEmailTextInputLayout.isEnabled = true
        loginPasswordTextInputLayout.isEnabled = true
        loginButton.isEnabled = true
    }

    private fun disableInputs() {
        loginEmailTextInputLayout.isEnabled = false
        loginPasswordTextInputLayout.isEnabled = false
        loginButton.isEnabled = false
    }

    private fun attemptLogin() {
        val email = loginEmailInput.text.toString()
        val password = loginPasswordInput.text.toString()
        if (email.isNotBlank() && password.isNotBlank()) {
            viewModel.login(email, password)
        }
    }
}
