package com.cloudfiveapp.android.ui.productslist.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cloudfiveapp.android.ui.productslist.model.ProductsListContract
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsListViewModelFactory
@Inject constructor(private val repository: ProductsListContract.Repository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
