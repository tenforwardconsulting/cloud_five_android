package com.cloudfiveapp.android.ui.releaseslist.di

import android.app.DownloadManager
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.releaseslist.ReleasesAdapter
import com.cloudfiveapp.android.ui.releaseslist.data.MockOrderedWithDelayReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesRepository
import com.cloudfiveapp.android.ui.releaseslist.viewmodel.ReleasesListViewModelFactory
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import javax.inject.Named

@Module
@ReleasesListScope
class ReleasesListModule {

    @Provides
    @ReleasesListScope
    fun viewModelFactory(application: CloudFiveApp,
                         repository: ReleasesListContract.Repository,
                         downloadManager: DownloadManager,
                         compositeDisposable: CompositeDisposable)
            : ReleasesListViewModelFactory {
        return ReleasesListViewModelFactory(application, repository, downloadManager, compositeDisposable)
    }

    @Provides
    @ReleasesListScope
    fun releasesRepo(@Named("mock") releasesApi: ReleasesApi): ReleasesListContract.Repository {
        return ReleasesRepository(releasesApi)
    }

    @Provides
    @ReleasesListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @ReleasesListScope
    fun adapter(): ReleasesAdapter = ReleasesAdapter()

    @Provides
    @ReleasesListScope
    fun releasesApi(retrofit: Retrofit): ReleasesApi {
        return retrofit.create(ReleasesApi::class.java)
    }

    // Mock

    @Provides
    @Named("mock")
    @ReleasesListScope
    fun mockOrderedReleasesApi(releasesApiBehaviorDelegate: BehaviorDelegate<ReleasesApi>): ReleasesApi {
        return MockOrderedWithDelayReleasesApi(releasesApiBehaviorDelegate)
    }

    @Provides
    @ReleasesListScope
    fun providesReleasesApiBehaviorDelegate(mockRetrofit: MockRetrofit): BehaviorDelegate<ReleasesApi> {
        return mockRetrofit.create(ReleasesApi::class.java)
    }
}
