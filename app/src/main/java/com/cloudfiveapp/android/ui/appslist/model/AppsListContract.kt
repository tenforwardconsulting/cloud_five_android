package com.cloudfiveapp.android.ui.appslist.model

import com.cloudfiveapp.android.ui.appslist.data.App
import io.reactivex.Observable

class AppsListContract private constructor() {

    interface Repository {
        val apps: Observable<List<App>>
    }
}
