package com.cloudfiveapp.android.ui.productslist

import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Product
import com.cloudfiveapp.android.data.remote.ProductsApi
import com.cloudfiveapp.android.util.extensions.toResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductsRepository
@Inject constructor(@Named("mock") private val productsApi: ProductsApi,
                    private val errorConverter: ApiErrorConverter)
    : ProductsListContract.Repository {

    override val productsOutcome: PublishSubject<Outcome<List<Product>>> = PublishSubject.create()

    override fun refreshProducts() {
        productsOutcome.onNext(Outcome.loading(true))
        productsApi.getProducts()
                .map { it.toResult(errorConverter) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            productsOutcome.onNext(Outcome.loading(false))
                            productsOutcome.onNext(it)
                        },
                        onError = { error ->
                            productsOutcome.onNext(Outcome.loading(false))
                            productsOutcome.onNext(Outcome.error(error))
                        })
    }
}
