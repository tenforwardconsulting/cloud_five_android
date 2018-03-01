package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ReleasesRepository
@Inject constructor(@Named("mock") private val releasesApi: ReleasesApi)
    : ReleasesListContract.Repository {

    private val refreshes = PublishSubject.create<Unit>()

    override fun getReleases(productId: String): Observable<List<Release>> {
        return refreshes
                .startWith(Unit)
                .flatMapSingle {
                    releasesApi.getReleases(productId)
                }
                .filter { response ->
                    response.isSuccessful
                }
                .map { it.body()!! }
    }

    override fun refresh() {
        refreshes.onNext(Unit)
    }
}
