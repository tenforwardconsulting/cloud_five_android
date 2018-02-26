package com.cloudfiveapp.android.ui.productslist.di

import com.cloudfiveapp.android.application.di.AppComponent
import com.cloudfiveapp.android.ui.productslist.ProductsListActivity
import dagger.Component

@ProductsListScope
@Component(dependencies = [AppComponent::class], modules = [ProductsListModule::class])
interface ProductsListComponent {

    fun inject(productsListActivity: ProductsListActivity)
}
