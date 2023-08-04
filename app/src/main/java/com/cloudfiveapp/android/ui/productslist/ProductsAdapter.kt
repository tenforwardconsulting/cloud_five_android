package com.cloudfiveapp.android.ui.productslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.data.model.Product
import com.cloudfiveapp.android.databinding.RowProductBinding
import com.cloudfiveapp.android.util.extensions.inflate

class ProductsAdapter : ListAdapter<Product, ProductViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {

            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    var interactor: ProductsInteractor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemBinding = RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), interactor)
    }
}
