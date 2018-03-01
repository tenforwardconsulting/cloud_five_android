package com.cloudfiveapp.android.application.di

import com.cloudfiveapp.android.ui.productslist.model.ProductsListContract
import com.cloudfiveapp.android.ui.productslist.model.ProductsRepository
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesProductsRepository(productsRepository: ProductsRepository): ProductsListContract.Repository {
        return productsRepository
    }

    @Provides
    @Singleton
    fun providesReleasesRepository(releasesRepository: ReleasesRepository): ReleasesListContract.Repository {
        return releasesRepository
    }
}
