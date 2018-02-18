package com.cloudfiveapp.android.application.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class MockNetworkModule {

    @Provides
    @Singleton
    fun providesMockRetrofit(retrofit: Retrofit, networkBehavior: NetworkBehavior): MockRetrofit {
        return MockRetrofit.Builder(retrofit)
                .networkBehavior(networkBehavior)
                .build()
    }

    @Provides
    @Singleton
    fun providesNetworkBehavior(): NetworkBehavior {
        val networkBehavior = NetworkBehavior.create()
        networkBehavior.setDelay(1, TimeUnit.SECONDS)
        networkBehavior.setVariancePercent(30)
        networkBehavior.setFailurePercent(0)
        return networkBehavior
    }
}
