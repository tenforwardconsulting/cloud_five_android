package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.releaseslist.data.Release
import io.reactivex.Observable

class ReleasesListContract private constructor() {

    interface Repository {
        fun getReleases(productId: String): Observable<List<Release>>
        fun refresh()
    }
}
