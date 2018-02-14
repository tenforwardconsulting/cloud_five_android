package com.cloudfiveapp.android.ui.releaseslist.di

import com.cloudfiveapp.android.ui.releaseslist.ReleasesAdapter
import com.cloudfiveapp.android.ui.releaseslist.data.MockReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
@ReleasesListScope
class ReleasesListModule {

    @Provides
    @ReleasesListScope
    fun releasesRepo(@Named("mock") releasesApi: ReleasesApi): ReleasesListContract.Repository {
        return ReleasesRepository(releasesApi)
    }

    @Provides
    @ReleasesListScope
    fun adapter(): ReleasesAdapter {
        return ReleasesAdapter()
    }

    @Provides
    @Named("real")
    @ReleasesListScope
    fun releasesApi(retrofit: Retrofit): ReleasesApi {
        return retrofit.create(ReleasesApi::class.java)
    }

    @Provides
    @Named("mock")
    @ReleasesListScope
    fun mockReleasesApi(): ReleasesApi {
        return MockReleasesApi()
    }
}
