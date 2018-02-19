package com.cloudfiveapp.android.ui.login

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
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

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        component.inject(this)
        lifecycle.addObserver(CloudAnimator(loginParentView, layoutInflater))

        loginButton.setOnClickListener {
            val email = loginEmailInput.text.toString()
            val password = loginPasswordInput.text.toString()
            if (email.isNotBlank() && password.isNotBlank()) {
                viewModel.login(email, password)
            }
        }

        loginEmailInput.addOnTextChangedListener {
            loginEmailTextInputLayout.error = null
            loginPasswordTextInputLayout.error = null
        }

        loginPasswordInput.addOnTextChangedListener {
            loginEmailTextInputLayout.error = null
            loginPasswordTextInputLayout.error = null
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loginResponses
                .doOnSubscribe { compositeDisposable += it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = this::render)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun render(loginViewState: LoginViewModel.LoginViewState) {
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
