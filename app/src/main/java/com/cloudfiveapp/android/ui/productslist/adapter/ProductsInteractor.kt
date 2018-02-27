package com.cloudfiveapp.android.ui.productslist.adapter

import com.cloudfiveapp.android.ui.productslist.data.Product

interface ProductsInteractor {
    fun onProductClick(product: Product)
}
