package com.cloudfiveapp.android.ui.releaseslist.di

import android.app.DownloadManager
import android.content.Context
import com.cloudfiveapp.android.ui.common.networking.ApiErrorConverter
import com.cloudfiveapp.android.ui.releaseslist.adapter.ReleasesAdapter
import com.cloudfiveapp.android.ui.releaseslist.data.MockOrderedReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import com.cloudfiveapp.android.ui.releaseslist.model.DownloadManagerApkDownloader
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
    fun viewModelFactory(repository: ReleasesListContract.Repository,
                         apkDownloader: ReleasesListContract.ApkDownloader,
                         compositeDisposable: CompositeDisposable)
            : ReleasesListViewModelFactory {
        return ReleasesListViewModelFactory(repository, apkDownloader, compositeDisposable)
    }

    @Provides
    @ReleasesListScope
    fun releasesRepo(@Named("mock") releasesApi: ReleasesApi,
                     errorConverter: ApiErrorConverter,
                     compositeDisposable: CompositeDisposable)
            : ReleasesListContract.Repository {
        return ReleasesRepository(releasesApi, errorConverter, compositeDisposable)
    }

    @Provides
    @ReleasesListScope
    fun apkDownloader(context: Context, downloadManager: DownloadManager,
                      compositeDisposable: CompositeDisposable): ReleasesListContract.ApkDownloader {
        return DownloadManagerApkDownloader(context, downloadManager, compositeDisposable)
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
        return MockOrderedReleasesApi(releasesApiBehaviorDelegate)
    }

    @Provides
    @ReleasesListScope
    fun providesReleasesApiBehaviorDelegate(mockRetrofit: MockRetrofit): BehaviorDelegate<ReleasesApi> {
        return mockRetrofit.create(ReleasesApi::class.java)
    }
}
