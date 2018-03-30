package com.cloudfiveapp.android.application.injection

import com.cloudfiveapp.android.BuildConfig
import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.model.ApiError
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule(private val cache: Cache) {

    @Provides
    @Singleton
    fun providesRetrofit(@Named("baseUrl") baseUrl: String,
                         moshiConverterFactory: MoshiConverterFactory,
                         okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(moshiConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun providesApiErrorConverter(retrofit: Retrofit): ApiErrorConverter {
        return retrofit.responseBodyConverter<ApiError>(ApiError::class.java, emptyArray())
    }

    @Provides
    @Named("baseUrl")
    fun providesBaseUrl(): String {
        return "https://www.example.com/api/"
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder().cache(cache)
        if (BuildConfig.DEBUG) {
            client.addNetworkInterceptor(StethoInterceptor())
        }
        return client.build()
    }

    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory()) // Add last
                .build()
    }

    @Provides
    @Singleton
    fun providesMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }
}
