package com.cloudfiveapp.android.application.di

import android.content.Context
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun context(): Context

    fun retrofit(): Retrofit
}
