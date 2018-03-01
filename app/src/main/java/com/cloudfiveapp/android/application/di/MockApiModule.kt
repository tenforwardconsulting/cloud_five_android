package com.cloudfiveapp.android.application.di

import com.cloudfiveapp.android.ui.login.data.LoginApi
import com.cloudfiveapp.android.ui.login.data.MockLoginApi
import dagger.Module
import dagger.Provides
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class MockApiModule {

    @Provides
    @Singleton
    @Named("mock")
    fun providesMockLoginApi(loginApiBehaviorDelegate: BehaviorDelegate<LoginApi>): LoginApi {
        return MockLoginApi(loginApiBehaviorDelegate)
    }

    @Provides
    @Singleton
    fun providesLoginApiBehaviorDelegate(mockRetrofit: MockRetrofit): BehaviorDelegate<LoginApi> {
        return mockRetrofit.create(LoginApi::class.java)
    }
}
