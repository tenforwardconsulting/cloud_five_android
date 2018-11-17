package com.cloudfiveapp.android.ui.productslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseFragment
import com.cloudfiveapp.android.application.injection.Injector
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Product
import com.cloudfiveapp.android.util.extensions.get
import com.cloudfiveapp.android.util.extensions.toastNetworkError
import com.cloudfiveapp.android.util.extensions.visible
import kotlinx.android.synthetic.main.fragment_products_list.view.*

class ProductsListFragment
    : BaseFragment(),
      ProductsInteractor {

    private val adapter = ProductsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.interactor = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.productsRecycler.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = Injector.get().productsListViewModelFactory()
        val viewModel = viewModelFactory.get(this, ProductsListViewModel::class)

        bindToViewModel(viewModel)
    }

    private fun bindToViewModel(viewModel: ProductsListViewModel) {
        view?.productsSwipeRefresh?.setOnRefreshListener { viewModel.refreshProducts() }

        viewModel.getProducts()

        viewModel.products.observe(viewLifecycleOwner, Observer { outcome ->
            val view = view ?: return@Observer
            when (outcome) {
                is Outcome.Loading -> {
                    view.productsSwipeRefresh.isRefreshing = outcome.loading
                }
                is Outcome.Success -> {
                    adapter.submitList(outcome.data)
                }
                is Outcome.Error -> {
                    context?.toastNetworkError(outcome.message ?: outcome.error?.message)
                }
            }
            view.productsEmptyText.visible(adapter.itemCount == 0)
        })
    }

    // region ProductsInteractor

    override fun onProductClick(product: Product) {
        val action = ProductsListFragmentDirections
                .actionProductsListFragmentToReleasesListFragment(product.id)
        view?.findNavController()?.navigate(action)
    }

    // endregion
}
