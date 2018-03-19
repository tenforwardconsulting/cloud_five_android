package com.cloudfiveapp.android.ui.productslist

import com.cloudfiveapp.android.data.model.Product

interface ProductsInteractor {
    fun onProductClick(product: Product)
}
