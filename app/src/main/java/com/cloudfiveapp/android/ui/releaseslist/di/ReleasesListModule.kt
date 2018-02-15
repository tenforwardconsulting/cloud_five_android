package com.cloudfiveapp.android.ui.releaseslist.di

import android.app.DownloadManager
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.releaseslist.ReleasesAdapter
import com.cloudfiveapp.android.ui.releaseslist.data.MockOrderedWithDelayReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.data.MockRandomReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesRepository
import com.cloudfiveapp.android.ui.releaseslist.viewmodel.ReleasesListViewModelFactory
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
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
    fun releasesRepo(@Named("mock.ordered") releasesApi: ReleasesApi): ReleasesListContract.Repository {
        return ReleasesRepository(releasesApi)
    }

    @Provides
    @ReleasesListScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @ReleasesListScope
    fun adapter(): ReleasesAdapter = ReleasesAdapter()

    @Provides
    @Named("real")
    @ReleasesListScope
    fun releasesApi(retrofit: Retrofit): ReleasesApi = retrofit.create(ReleasesApi::class.java)

    @Provides
    @Named("mock.random")
    @ReleasesListScope
    fun mockReleasesApi(): ReleasesApi = MockRandomReleasesApi()

    @Provides
    @Named("mock.ordered")
    @ReleasesListScope
    fun mockOrderedReleasesApi(): ReleasesApi = MockOrderedWithDelayReleasesApi()
}
