package com.cloudfiveapp.android.ui.login.di

import com.cloudfiveapp.android.application.di.AppComponent
import com.cloudfiveapp.android.ui.login.LoginActivity
import dagger.Component

@LoginScope
@Component(dependencies = [AppComponent::class], modules = [LoginModule::class])
interface LoginComponent {

    fun inject(loginActivity: LoginActivity)
}
