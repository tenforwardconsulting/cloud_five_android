package com.cloudfiveapp.android.ui.productslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Product

class ProductsListViewModel(private val repository: ProductsListContract.Repository)
    : ViewModel() {

    val products: LiveData<Outcome<List<Product>>> = repository.productsOutcome

    fun getProducts() {
        if (products.value == null) {
            repository.refreshProducts()
        }
    }

    fun refreshProducts() {
        repository.refreshProducts()
    }
}
