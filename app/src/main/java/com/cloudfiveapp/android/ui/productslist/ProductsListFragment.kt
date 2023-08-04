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
import com.cloudfiveapp.android.databinding.FragmentProductsListBinding
import com.cloudfiveapp.android.util.extensions.get
import com.cloudfiveapp.android.util.extensions.toastNetworkError
import com.cloudfiveapp.android.util.extensions.visible

class ProductsListFragment
    : BaseFragment(),
      ProductsInteractor {

    private val adapter = ProductsAdapter()
    private var _binding: FragmentProductsListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.interactor = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProductsListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productsRecycler.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModelFactory = Injector.get().productsListViewModelFactory()
        val viewModel = viewModelFactory.get(this, ProductsListViewModel::class)

        bindToViewModel(viewModel)
    }

    private fun bindToViewModel(viewModel: ProductsListViewModel) {
        binding.productsSwipeRefresh.setOnRefreshListener { viewModel.refreshProducts() }

        viewModel.getProducts()

        viewModel.products.observe(viewLifecycleOwner, Observer { outcome ->
            val view = view ?: return@Observer
            when (outcome) {
                is Outcome.Loading -> {
                    binding.productsSwipeRefresh.isRefreshing = outcome.loading
                }
                is Outcome.Success -> {
                    adapter.submitList(outcome.data)
                }
                is Outcome.Error -> {
                    context?.toastNetworkError(outcome.message ?: outcome.error?.message)
                }
            }
            binding.productsEmptyText.visible(adapter.itemCount == 0)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // region ProductsInteractor

    override fun onProductClick(product: Product) {
        val action = ProductsListFragmentDirections
                .actionProductsListFragmentToReleasesListFragment(product.id)
        view?.findNavController()?.navigate(action)
    }

    // endregion
}
