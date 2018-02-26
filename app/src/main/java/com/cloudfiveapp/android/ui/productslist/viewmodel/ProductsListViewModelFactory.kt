package com.cloudfiveapp.android.ui.productslist.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.cloudfiveapp.android.ui.productslist.di.ProductsListScope
import javax.inject.Inject

@ProductsListScope
class ProductsListViewModelFactory @Inject constructor()
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
