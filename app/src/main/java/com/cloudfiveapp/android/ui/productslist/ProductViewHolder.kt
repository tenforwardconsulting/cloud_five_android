package com.cloudfiveapp.android.ui.productslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cloudfiveapp.android.data.model.Product
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_product.view.*

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    override val containerView: View = itemView

    fun bind(product: Product, interactor: ProductsInteractor?) {
        itemView.productName.text = product.name
        itemView.setOnClickListener { interactor?.onProductClick(product) }
    }
}
