package com.cloudfiveapp.android.application.injection

import com.cloudfiveapp.android.ui.productslist.ProductsListContract
import com.cloudfiveapp.android.ui.productslist.ProductsRepository
import com.cloudfiveapp.android.ui.releaseslist.ReleasesListContract
import com.cloudfiveapp.android.ui.releaseslist.ReleasesRepository
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
