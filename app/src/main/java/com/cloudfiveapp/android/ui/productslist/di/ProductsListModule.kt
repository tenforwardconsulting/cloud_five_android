package com.cloudfiveapp.android.ui.productslist.di

import com.cloudfiveapp.android.ui.common.networking.ApiErrorConverter
import com.cloudfiveapp.android.ui.productslist.adapter.ProductsAdapter
import com.cloudfiveapp.android.ui.productslist.data.MockProductsApi
import com.cloudfiveapp.android.ui.productslist.data.ProductsApi
import com.cloudfiveapp.android.ui.productslist.model.ProductsListContract
import com.cloudfiveapp.android.ui.productslist.model.ProductsRepository
import com.cloudfiveapp.android.ui.productslist.viewmodel.ProductsListViewModelFactory
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import javax.inject.Named

@Module
@ProductsListScope
class ProductsListModule {

    @Provides
    @ProductsListScope
    fun providesViewModelFactory(repository: ProductsListContract.Repository,
                                 compositeDisposable: CompositeDisposable)
            : ProductsListViewModelFactory {
        return ProductsListViewModelFactory(repository, compositeDisposable)
    }

    @Provides
    @ProductsListScope
    fun providesProductsAdapter() = ProductsAdapter()

    @Provides
    @ProductsListScope
    fun providesProductsRepository(@Named("mock") productsApi: ProductsApi,
                                   errorConverter: ApiErrorConverter,
                                   compositeDisposable: CompositeDisposable)
            : ProductsListContract.Repository {
        return ProductsRepository(productsApi, errorConverter, compositeDisposable)
    }

    @Provides
    @ProductsListScope
    fun providesCompositeDisposable() = CompositeDisposable()

    @Provides
    @ProductsListScope
    fun providesProductsApi(retrofit: Retrofit): ProductsApi {
        return retrofit.create(ProductsApi::class.java)
    }

    // Mock

    @Provides
    @Named("mock")
    @ProductsListScope
    fun providesMockProductsApi(delegate: BehaviorDelegate<ProductsApi>): ProductsApi {
        return MockProductsApi(delegate)
    }

    @Provides
    @ProductsListScope
    fun providesProductsApiDelegate(mockRetrofit: MockRetrofit): BehaviorDelegate<ProductsApi> {
        return mockRetrofit.create(ProductsApi::class.java)
    }
}
