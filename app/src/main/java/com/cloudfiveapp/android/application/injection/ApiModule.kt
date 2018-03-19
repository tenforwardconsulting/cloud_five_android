package com.cloudfiveapp.android.application.injection

import com.cloudfiveapp.android.data.remote.LoginApi
import com.cloudfiveapp.android.data.remote.ProductsApi
import com.cloudfiveapp.android.data.remote.ReleasesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun providesLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun providesProductsApi(retrofit: Retrofit): ProductsApi {
        return retrofit.create(ProductsApi::class.java)
    }

    @Provides
    @Singleton
    fun releasesApi(retrofit: Retrofit): ReleasesApi {
        return retrofit.create(ReleasesApi::class.java)
    }
}
