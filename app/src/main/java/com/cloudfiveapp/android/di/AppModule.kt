package com.cloudfiveapp.android.di

import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.android.api.AuthService
import com.cloudfiveapp.android.api.HeadersInterceptor
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesBaseUrl(): String = "${BuildConfig.BASE_URL}/app_api/"

    @Provides
    fun providesLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level = BuildConfig.HTTP_LOG_LEVEL
        }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        headersInterceptor: HeadersInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
    ) = OkHttpClient.Builder()
        .addInterceptor(headersInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun providesMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun providesRetrofit(
        baseUrl: String,
        client: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(moshiConverterFactory)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService = retrofit.create()
}
