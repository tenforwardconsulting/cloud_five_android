package com.cloudfiveapp.android.ui.productslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cloudfiveapp.android.data.model.Product
import com.cloudfiveapp.android.databinding.RowProductBinding
import kotlinx.android.extensions.LayoutContainer

class ProductViewHolder(private val itemBinding: RowProductBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(product: Product, interactor: ProductsInteractor?) {
        itemBinding.productName.text = product.name
        itemBinding.root.setOnClickListener { interactor?.onProductClick(product) }
    }
}
