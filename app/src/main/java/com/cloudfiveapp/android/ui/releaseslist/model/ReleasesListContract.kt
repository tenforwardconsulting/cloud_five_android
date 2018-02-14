package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.releaseslist.data.Release
import io.reactivex.Observable

class ReleasesListContract private constructor() {

    interface Repository {
        val releases: Observable<List<Release>>
    }
}
