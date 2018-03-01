package com.cloudfiveapp.android.ui.login

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.login.di.DaggerLoginComponent
import com.cloudfiveapp.android.ui.login.viewmodel.LoginViewModel
import com.cloudfiveapp.android.ui.login.viewmodel.LoginViewModel.LoginViewState.*
import com.cloudfiveapp.android.ui.login.viewmodel.LoginViewModelFactory
import com.cloudfiveapp.android.util.CloudAnimator
import com.cloudfiveapp.android.util.extensions.addOnTextChangedListener
import com.cloudfiveapp.android.util.extensions.get
import com.cloudfiveapp.android.util.extensions.showKeyboard
import com.cloudfiveapp.android.util.extensions.toast
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private val component by lazy {
        DaggerLoginComponent.builder().appComponent(CloudFiveApp.appComponent).build()
    }

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private val viewModel by lazy {
        viewModelFactory.get(this, LoginViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        component.inject(this)
        lifecycle.addObserver(CloudAnimator(loginParentView, layoutInflater))

        loginEmailInput.addOnTextChangedListener {
            loginEmailTextInputLayout.error = null
            loginPasswordTextInputLayout.error = null
        }

        loginPasswordInput.addOnTextChangedListener {
            loginEmailTextInputLayout.error = null
            loginPasswordTextInputLayout.error = null
        }

        bindToViewModel()
    }

    private fun bindToViewModel() {
        viewModel.loginViewState.observe(this, Observer { loginViewState ->
            Timber.d("$loginViewState")
            when (loginViewState) {
                is Loading -> {
                    disableInputs()
                }
                is LoggedIn -> {
                    toast("Login success!")
                    enableInputs()
                    finish()
                }
                is InvalidCredentials -> {
                    loginEmailTextInputLayout.error = loginViewState.message
                    loginPasswordTextInputLayout.error = loginViewState.message
                    enableInputs()
                    loginEmailInput.showKeyboard()
                }
                is NetworkError -> {
                    toast("Network error")
                    enableInputs()
                }
            }
        })

        loginButton.setOnClickListener {
            val email = loginEmailInput.text.toString()
            val password = loginPasswordInput.text.toString()
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.login(email, password)
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
}
