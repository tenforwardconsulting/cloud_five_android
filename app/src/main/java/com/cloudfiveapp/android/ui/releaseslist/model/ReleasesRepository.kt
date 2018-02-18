package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.releaseslist.data.ProductId
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ReleasesRepository(private val releasesApi: ReleasesApi)
    : ReleasesListContract.Repository {

    private val refreshes = PublishSubject.create<Unit>()
    private var cache: MutableMap<ProductId, List<Release>> = mutableMapOf()

    override fun getReleases(productId: String): Observable<List<Release>> {
        return refreshes
                .flatMapSingle {
                    releasesApi.getReleases(productId)
                }
                .filter { response ->
                    response.isSuccessful
                }
                .map { it.body()!! }
                .doOnNext { cache[productId] = it }
                .startWith(cache[productId] ?: emptyList())
    }

    override fun refresh() {
        refreshes.onNext(Unit)
        cache.clear()
    }
}
