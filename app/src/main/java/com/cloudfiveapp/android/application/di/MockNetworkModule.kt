package com.cloudfiveapp.android.application.di

import dagger.Module
import dagger.Provides
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
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
        return NetworkBehavior.create().apply {
            setDelay(1, TimeUnit.SECONDS)
            setVariancePercent(30)
            setFailurePercent(3)
            setErrorPercent(10)
            setErrorFactory {
                Response.error<Any>(404,
                        ResponseBody.create(
                                MediaType.parse("application/json"),
                                "{ \"status\": 404, \"message\": \"This is an error\" }"))
            }
        }
    }
}
