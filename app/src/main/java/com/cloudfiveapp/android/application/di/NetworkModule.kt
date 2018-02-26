package com.cloudfiveapp.android.application.di

import android.content.Context
import com.cloudfiveapp.android.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(@Named("baseUrl") baseUrl: String,
                         moshiConverterFactory: MoshiConverterFactory,
                         rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                         okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(moshiConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Named("baseUrl")
    fun providesBaseUrl(): String {
        return "https://www.example.com/api/"
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache,
                             loggingInterceptor: HttpLoggingInterceptor,
                             headerInterceptor: Interceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
        if (BuildConfig.DEBUG) {
            client.addNetworkInterceptor(StethoInterceptor())
        }
        return client.build()
    }

    @Provides
    @Singleton
    fun providesHeadersInterceptor(@Named("apiVersion") apiVersion: String): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                    .addHeader("Accept", apiVersion)
                    .addHeader("X-Cloud-Five-Platform", "android")
                    .addHeader("X-Cloud-Five-Device-Name", android.os.Build.MODEL)
                    .addHeader("X-Cloud-Five-Device-System-Version", android.os.Build.VERSION.RELEASE)
                    .addHeader("X-Cloud-Five-App-Version", BuildConfig.VERSION_NAME)
                    .addHeader("X-Cloud-Five-App-Build-Number", BuildConfig.VERSION_CODE.toString())
                    .addHeader("X-Cloud-Five-Git-Commit", BuildConfig.GIT_HASH)
                    .build()
            chain.proceed(request)
        }
    }

    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Named("apiVersion")
    fun providesApiVersion() = "application/vnd.cloudfive.v1"

    @Provides
    @Singleton
    fun providesOkHttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(context.cacheDir, cacheSize.toLong())
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

    @Provides
    @Singleton
    fun providesRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }
}
