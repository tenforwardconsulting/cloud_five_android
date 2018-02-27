package com.cloudfiveapp.android.ui.productslist.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.cloudfiveapp.android.ui.productslist.data.Product
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_product.view.*

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View = itemView

    fun bind(product: Product, interactor: ProductInteractor?) {
        itemView.productName.text = product.name
        itemView.setOnClickListener { interactor?.onProductClick(product) }
    }
}
