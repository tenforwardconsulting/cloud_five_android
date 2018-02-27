package com.cloudfiveapp.android.ui.productslist.model

import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.productslist.data.Product
import io.reactivex.subjects.PublishSubject

class ProductsListContract private constructor() {

    interface Repository {
        val productsOutcome: PublishSubject<Outcome<List<Product>>>
        fun refreshProducts()
    }
}
