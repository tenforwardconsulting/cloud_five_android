package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ReleasesRepository(private val releasesApi: ReleasesApi)
    : ReleasesListContract.Repository {

    override fun getReleases(productId: String): Observable<List<Release>> {
        return Observable.interval(0, 5, TimeUnit.SECONDS)
                .flatMapSingle {
                    releasesApi.getReleases("x")
                }
    }
}
