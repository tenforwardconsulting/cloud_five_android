package com.cloudfiveapp.android.ui.productslist

import android.arch.lifecycle.LiveData
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Product

class ProductsListContract private constructor() {

    interface Repository {
        val productsOutcome: LiveData<Outcome<List<Product>>>
        fun refreshProducts()
    }
}
