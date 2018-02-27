package com.cloudfiveapp.android.ui.releaseslist.model

import android.content.Intent
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ReleasesListContract private constructor() {

    interface Repository {
        val releasesOutcome: PublishSubject<Outcome<List<Release>>>
        fun refreshReleasesFor(productId: String)
    }

    interface ApkDownloader {

        val downloadEvents: Observable<DownloadEvent>

        fun downloadRelease(release: Release)

        fun downloadComplete(intent: Intent)

        fun openReleaseFile(release: Release)

        sealed class DownloadEvent(val release: Release?) {
            object DownloadNone : DownloadEvent(null)
            class DownloadStarted(release: Release) : DownloadEvent(release)
            class DownloadCompleted(release: Release) : DownloadEvent(release)
        }
    }
}
