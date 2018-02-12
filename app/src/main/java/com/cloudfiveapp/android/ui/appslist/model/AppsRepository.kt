package com.cloudfiveapp.android.ui.appslist.model

import com.cloudfiveapp.android.ui.appslist.data.App
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class AppsRepository : AppsListContract.Repository {

    override val apps: Observable<List<App>>
        get() {
            return Observable.merge(
                    Observable.just(listOf(
                            App("Ten Forward Dev", "v3.1", "174", "resident_app_android", "de38a51e"),
                            App("Ten Forward QA", "v3.1", "209", "resident_app_android", "de38a51e"))),
                    Observable.just(listOf(
                            App("Ten Forward Dev", "v3.2", "178", "resident_app_android", "de38a51e"),
                            App("Ten Forward QA", "v3.2", "215", "resident_app_android", "de38a51e")))
                            .delay(3, TimeUnit.SECONDS))
        }

}
