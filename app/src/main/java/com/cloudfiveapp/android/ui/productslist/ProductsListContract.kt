package com.cloudfiveapp.android.ui.productslist

import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Product
import io.reactivex.subjects.PublishSubject

class ProductsListContract private constructor() {

    interface Repository {
        val productsOutcome: PublishSubject<Outcome<List<Product>>>
        fun refreshProducts()
    }
}
