package com.cloudfiveapp.android.ui.productslist.model

import com.cloudfiveapp.android.ui.common.networking.ApiErrorConverter
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.productslist.data.Product
import com.cloudfiveapp.android.ui.productslist.data.ProductsApi
import com.cloudfiveapp.android.util.extensions.toResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ProductsRepository(private val productsApi: ProductsApi,
                         private val errorConverter: ApiErrorConverter,
                         private val compositeDisposable: CompositeDisposable)
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
                .addTo(compositeDisposable)
    }
}