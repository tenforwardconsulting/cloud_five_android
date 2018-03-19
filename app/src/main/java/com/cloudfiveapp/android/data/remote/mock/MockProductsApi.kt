package com.cloudfiveapp.android.data.remote.mock

import com.cloudfiveapp.android.data.model.Product
import com.cloudfiveapp.android.data.remote.ProductsApi
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.mock.BehaviorDelegate

class MockProductsApi(private val delegate: BehaviorDelegate<ProductsApi>)
    : ProductsApi {

    companion object {

        private val products = listOf(
                Product("1", "Mobile Doorman", "1", "Mobile Doorman"),
                Product("2", "Staff App", "1", "Mobile Doorman"),
                Product("3", "Virtualbadge V2", "2", "VirtualBadge"),
                Product("4", "Cloud Five App", "3", "Cloud Five Apps"))
    }

    override fun getProducts(): Single<Result<List<Product>>> {
        return delegate.returningResponse(products).getProducts()
    }
}
