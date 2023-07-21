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
import com.cloudfiveapp.android.databinding.ActivityLoginBinding
import com.cloudfiveapp.android.push.PushManager
import com.cloudfiveapp.android.ui.main.MainActivity
import com.cloudfiveapp.android.ui.main.SessionManager
import com.cloudfiveapp.android.util.CloudAnimator
import com.cloudfiveapp.android.util.extensions.get
import com.cloudfiveapp.android.util.extensions.showKeyboard
import com.cloudfiveapp.android.util.extensions.toast
import com.cloudfiveapp.android.util.extensions.toastNetworkError
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

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        lifecycle.addObserver(CloudAnimator(binding.loginParentView, layoutInflater))

        bindToViewModel()
    }

    private fun bindToViewModel() {
        viewModel.loginViewState.observe(this, Observer { outcome ->
            Timber.d("$outcome")
            when (outcome) {
                is Outcome.Loading -> {
                    disableInputs()
                    binding.loginEmailTextInputLayout.error = null
                    binding.loginPasswordTextInputLayout.error = null
                }
                is Outcome.Success -> {
                    toast("Login success!")
                    enableInputs()
                    SessionManager.logIn("auth_token")
                    PushManager.register(binding.loginEmailInput.text.toString())
                    startActivity(MainActivity.newIntent(this))
                    finish()
                }
                is Outcome.Error -> {
                    if (!outcome.message.isNullOrEmpty()) {
                        binding.loginEmailTextInputLayout.error = outcome.message
                        binding.loginPasswordTextInputLayout.error = outcome.message
                    } else {
                        toastNetworkError(outcome.error?.message)
                    }
                    enableInputs()
                    binding.loginEmailInput.showKeyboard()
                }
            }
        })

        binding.loginButton.setOnClickListener { attemptLogin() }

        binding.loginPasswordInput.setOnEditorActionListener { _, actionId, _ ->
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
        binding.loginEmailTextInputLayout.isEnabled = true
        binding.loginPasswordTextInputLayout.isEnabled = true
        binding.loginButton.isEnabled = true
    }

    private fun disableInputs() {
        binding.loginEmailTextInputLayout.isEnabled = false
        binding.loginPasswordTextInputLayout.isEnabled = false
        binding.loginButton.isEnabled = false
    }

    private fun attemptLogin() {
        val email = binding.loginEmailInput.text.toString()
        val password = binding.loginPasswordInput.text.toString()
        if (email.isNotBlank() && password.isNotBlank()) {
            viewModel.login(email, password)
        }
    }
}
