package com.cloudfiveapp.android.ui.releaseslist.di

import com.cloudfiveapp.android.ui.releaseslist.ReleasesAdapter
import com.cloudfiveapp.android.ui.releaseslist.data.MockOrderedReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.data.MockRandomReleasesApi
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
    fun releasesRepo(@Named("mock.ordered") releasesApi: ReleasesApi): ReleasesListContract.Repository {
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
    @Named("mock.random")
    @ReleasesListScope
    fun mockReleasesApi(): ReleasesApi {
        return MockRandomReleasesApi()
    }

    @Provides
    @Named("mock.ordered")
    @ReleasesListScope
    fun mockOrderedReleasesApi(): ReleasesApi {
        return MockOrderedReleasesApi()
    }
}
