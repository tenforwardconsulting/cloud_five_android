package com.cloudfiveapp.android.ui.productslist.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.productslist.data.Product
import com.cloudfiveapp.android.ui.productslist.model.ProductsListContract
import com.cloudfiveapp.android.util.extensions.toLiveData
import io.reactivex.disposables.CompositeDisposable

class ProductsListViewModel(private val repository: ProductsListContract.Repository)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val products: LiveData<Outcome<List<Product>>> by lazy {
        repository.productsOutcome.toLiveData(compositeDisposable)
    }

    fun getProducts() {
        if (products.value == null) {
            repository.refreshProducts()
        }
    }

    fun refreshProducts() {
        repository.refreshProducts()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
