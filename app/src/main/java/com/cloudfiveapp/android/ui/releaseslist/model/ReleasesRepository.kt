package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ReleasesRepository(private val releasesApi: ReleasesApi)
    : ReleasesListContract.Repository {

    private val refreshes = PublishSubject.create<Unit>()

    override fun getReleases(productId: String): Observable<List<Release>> {
        return refreshes.startWith(Unit)
                .flatMapSingle {
                    releasesApi.getReleases(productId)
                }
    }

    override fun refresh() {
        refreshes.onNext(Unit)
    }
}
