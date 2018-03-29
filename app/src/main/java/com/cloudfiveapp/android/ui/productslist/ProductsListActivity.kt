package com.cloudfiveapp.android.ui.productslist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.injection.Injector
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Product
import com.cloudfiveapp.android.ui.releaseslist.ReleasesListActivity
import com.cloudfiveapp.android.util.extensions.toast
import com.cloudfiveapp.android.util.extensions.toastNetworkError
import com.cloudfiveapp.android.util.extensions.visible
import kotlinx.android.synthetic.main.activity_products.*

class ProductsListActivity
    : BaseActivity(),
      ProductsInteractor {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ProductsListActivity::class.java)
        }
    }

    val viewModelFactory = Injector.get().productsListViewModelFactory()

    private val adapter = ProductsAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ProductsListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        productsRecycler.adapter = adapter
        adapter.interactor = this

        bindToViewModel()
        viewModel.getProducts()
    }

    private fun bindToViewModel() {
        productsSwipeRefresh.setOnRefreshListener { viewModel.refreshProducts() }

        viewModel.products.observe(this, Observer { outcome ->
            when (outcome) {
                is Outcome.Loading -> {
                    productsSwipeRefresh.isRefreshing = outcome.loading
                }
                is Outcome.Success -> {
                    adapter.submitList(outcome.data)
                }
                is Outcome.Error -> {
                    toastNetworkError(outcome.message ?: outcome.error?.message)
                }
            }
            productsEmptyText.visible(adapter.itemCount == 0)
        })
    }

    // region ProductsInteractor

    override fun onProductClick(product: Product) {
        toast("Clicked on $product")
        // TODO: pass in product ID
        startActivity(ReleasesListActivity.newIntent(this))
    }

    // endregion
}
