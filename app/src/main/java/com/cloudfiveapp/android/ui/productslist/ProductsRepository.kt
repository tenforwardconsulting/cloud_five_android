package com.cloudfiveapp.android.ui.productslist

import android.arch.lifecycle.MutableLiveData
import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Product
import com.cloudfiveapp.android.data.remote.ProductsApi
import com.cloudfiveapp.android.util.extensions.enqueue
import com.cloudfiveapp.android.util.extensions.toOutcome
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductsRepository
@Inject constructor(@Named("mock") private val productsApi: ProductsApi,
                    private val errorConverter: ApiErrorConverter)
    : ProductsListContract.Repository {

    override val productsOutcome = MutableLiveData<Outcome<List<Product>>>()

    override fun refreshProducts() {
        productsOutcome.value = Outcome.loading(true)
        productsApi.getProducts()
                .enqueue(
                        success = { response ->
                            productsOutcome.value = Outcome.loading(false)
                            productsOutcome.value = response.toOutcome(errorConverter)
                        },
                        failure = {
                            productsOutcome.value = Outcome.loading(false)
                            productsOutcome.value = Outcome.error(it)
                        })
    }
}
