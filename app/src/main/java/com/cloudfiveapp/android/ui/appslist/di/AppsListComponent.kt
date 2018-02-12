package com.cloudfiveapp.android.ui.appslist.di

import com.cloudfiveapp.android.application.di.AppComponent
import com.cloudfiveapp.android.ui.appslist.AppsListActivity
import dagger.Component

@AppsListScope
@Component(dependencies = [AppComponent::class], modules = [AppsListModule::class])
interface AppsListComponent {
    fun inject(appsListListActivity: AppsListActivity)
}

