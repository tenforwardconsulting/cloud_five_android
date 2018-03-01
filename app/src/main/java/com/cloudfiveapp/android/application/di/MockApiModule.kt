package com.cloudfiveapp.android.application.di

import com.cloudfiveapp.android.ui.login.data.LoginApi
import com.cloudfiveapp.android.ui.login.data.MockLoginApi
import com.cloudfiveapp.android.ui.productslist.data.MockProductsApi
import com.cloudfiveapp.android.ui.productslist.data.ProductsApi
import com.cloudfiveapp.android.ui.releaseslist.data.MockOrderedReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import dagger.Module
import dagger.Provides
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class MockApiModule {

    // region Login

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

    // endregion

    // region ProductsList

    @Provides
    @Singleton
    @Named("mock")
    fun providesMockProductsApi(delegate: BehaviorDelegate<ProductsApi>): ProductsApi {
        return MockProductsApi(delegate)
    }

    @Provides
    @Singleton
    fun providesProductsApiDelegate(mockRetrofit: MockRetrofit): BehaviorDelegate<ProductsApi> {
        return mockRetrofit.create(ProductsApi::class.java)
    }

    // endregion

    // region ReleasesList

    @Provides
    @Named("mock")
    @Singleton
    fun mockOrderedReleasesApi(releasesApiBehaviorDelegate: BehaviorDelegate<ReleasesApi>): ReleasesApi {
        return MockOrderedReleasesApi(releasesApiBehaviorDelegate)
    }

    @Provides
    @Singleton
    fun providesReleasesApiBehaviorDelegate(mockRetrofit: MockRetrofit): BehaviorDelegate<ReleasesApi> {
        return mockRetrofit.create(ReleasesApi::class.java)
    }

    // endregion
}
