package com.cloudfiveapp.android.ui.releaseslist.di

import com.cloudfiveapp.android.ui.releaseslist.ReleasesAdapter
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesRepository
import dagger.Module
import dagger.Provides

@Module
@ReleasesListScope
class ReleasesListModule {

    @Provides
    @ReleasesListScope
    fun releasesRepo(): ReleasesListContract.Repository {
        return ReleasesRepository()
    }

    @Provides
    @ReleasesListScope
    fun adapter(): ReleasesAdapter {
        return ReleasesAdapter()
    }
}
