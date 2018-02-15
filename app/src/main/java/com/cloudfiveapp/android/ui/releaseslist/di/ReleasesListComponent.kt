package com.cloudfiveapp.android.ui.releaseslist.di

import com.cloudfiveapp.android.application.di.AppComponent
import com.cloudfiveapp.android.ui.releaseslist.ReleasesListActivity
import dagger.Component

@ReleasesListScope
@Component(dependencies = [AppComponent::class], modules = [ReleasesListModule::class])
interface ReleasesListComponent {

    fun inject(releasesListListActivity: ReleasesListActivity)
}

