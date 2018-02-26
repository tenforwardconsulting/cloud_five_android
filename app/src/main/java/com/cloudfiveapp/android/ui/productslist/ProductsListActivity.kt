package com.cloudfiveapp.android.ui.productslist

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.productslist.di.DaggerProductsListComponent
import com.cloudfiveapp.android.ui.productslist.viewmodel.ProductsListViewModel
import com.cloudfiveapp.android.ui.productslist.viewmodel.ProductsListViewModelFactory
import com.cloudfiveapp.android.util.extensions.toast
import com.cloudfiveapp.android.util.extensions.visible
import kotlinx.android.synthetic.main.activity_products.*
import javax.inject.Inject

class ProductsListActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ProductsListActivity::class.java)
        }
    }

    private val component by lazy {
        DaggerProductsListComponent.builder().appComponent(CloudFiveApp.appComponent).build()
    }

    @Inject
    lateinit var viewModelFactory: ProductsListViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ProductsListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        component.inject(this)

        productsEmptyText.visible(true)

        toast(viewModel.text)
    }
}
