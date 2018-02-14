package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.releaseslist.data.Release
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class ReleasesRepository : ReleasesListContract.Repository {

    override val releases: Observable<List<Release>>
        get() {
            return Observable.merge(
                    Observable.just(listOf(
                            Release("Ten Forward Dev", "v3.1", "174", "resident_app_android", "de38a51e"),
                            Release("Ten Forward QA", "v3.1", "209", "resident_app_android", "de38a51e"))),
                    Observable.just(listOf(
                            Release("Ten Forward Dev", "v3.2", "178", "resident_app_android", "de38a51e"),
                            Release("Ten Forward QA", "v3.2", "215", "resident_app_android", "de38a51e")))
                            .delay(3, TimeUnit.SECONDS))
        }

}
