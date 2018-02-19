package com.cloudfiveapp.android.ui.login.di

import com.cloudfiveapp.android.ui.login.data.LoginApi
import com.cloudfiveapp.android.ui.login.data.MockLoginApi
import com.cloudfiveapp.android.ui.login.viewmodel.LoginViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import javax.inject.Named

@Module
@LoginScope
class LoginModule {

    @Provides
    @LoginScope
    fun providesViewModelFactory(@Named("mock") loginApi: LoginApi): LoginViewModelFactory {
        return LoginViewModelFactory(loginApi)
    }

    @Provides
    @LoginScope
    fun providesLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @LoginScope
    @Named("mock")
    fun providesMockLoginApi(loginApiBehaviorDelegate: BehaviorDelegate<LoginApi>): LoginApi {
        return MockLoginApi(loginApiBehaviorDelegate)
    }

    @Provides
    @LoginScope
    fun providesLoginApiBehaviorDelegate(mockRetrofit: MockRetrofit): BehaviorDelegate<LoginApi> {
        return mockRetrofit.create(LoginApi::class.java)
    }
}
