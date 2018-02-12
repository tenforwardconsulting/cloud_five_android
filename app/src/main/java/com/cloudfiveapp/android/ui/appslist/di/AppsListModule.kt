package com.cloudfiveapp.android.ui.appslist.di

import com.cloudfiveapp.android.ui.appslist.AppsAdapter
import com.cloudfiveapp.android.ui.appslist.model.AppsListContract
import com.cloudfiveapp.android.ui.appslist.model.AppsRepository
import dagger.Module
import dagger.Provides

@Module
@AppsListScope
class AppsListModule {

    @Provides
    @AppsListScope
    fun appsRepo(): AppsListContract.Repository {
        return AppsRepository()
    }

    @Provides
    @AppsListScope
    fun adapter(): AppsAdapter {
        return AppsAdapter()
    }
}
