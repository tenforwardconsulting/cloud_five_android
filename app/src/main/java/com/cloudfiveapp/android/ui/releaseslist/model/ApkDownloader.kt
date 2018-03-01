package com.cloudfiveapp.android.ui.releaseslist.model

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.cloudfiveapp.android.application.CloudFiveFileProvider
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.util.MIME_TYPE_APK
import com.cloudfiveapp.android.util.extensions.enqueue
import com.cloudfiveapp.android.util.extensions.toUri
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApkDownloader
@Inject constructor(private val context: Context,
                    private val downloadManager: DownloadManager) {

    // TODO: is this ok?
    private val compositeDisposable = CompositeDisposable()

    private val downloads = mutableMapOf<Long, Release>()

    private val downloadEventsSubject = PublishSubject.create<DownloadEvent>()
    val downloadEvents: Observable<DownloadEvent>
        get() = downloadEventsSubject.startWith(DownloadEvent.DownloadNone)

    fun downloadRelease(release: Release) {
        Completable
                .fromRunnable {
                    val downloadId = downloadManager.enqueue(release.downloadUrl.toUri()) {
                        val file = getDestinationFile(release)
                        setDestinationUri(Uri.fromFile(file))
                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        setMimeType(MIME_TYPE_APK)
                        setTitle("${release.name} Download")
                        setDescription("${release.version} - ${release.latestBuildNumber} - ${release.commitHash}")
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    }
                    Timber.d("enqueued: $downloadId")
                    downloads[downloadId] = release
                    downloadEventsSubject.onNext(DownloadEvent.DownloadStarted(release))
                }
                .doOnSubscribe { compositeDisposable += it }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun downloadComplete(intent: Intent) {
        val downloadId = intent.extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
        Timber.d("completed: $downloadId")
        downloads[downloadId]?.let {
            downloadEventsSubject.onNext(DownloadEvent.DownloadCompleted(it))
        }
    }

    fun openReleaseFile(release: Release) {
        Completable
                .fromRunnable {
                    val file = getDestinationFile(release)
                    val uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        CloudFiveFileProvider.getUriForFile(context, file)
                    } else {
                        Uri.parse("file://${file.absolutePath}")
                    }

                    val intent = Intent(Intent.ACTION_VIEW)
                            .setDataAndType(uri, MIME_TYPE_APK)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    or Intent.FLAG_ACTIVITY_NEW_TASK
                                    or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    context.startActivity(intent)
                }
                .doOnSubscribe { compositeDisposable += it }
                .subscribeOn(Schedulers.io())
                .subscribe()

    }

    private fun getDestinationFile(release: Release): File {
        return CloudFiveFileProvider.getApkFile(context, release.downloadFileName)
    }

    sealed class DownloadEvent(val release: Release?) {
        object DownloadNone : DownloadEvent(null)
        class DownloadStarted(release: Release) : DownloadEvent(release)
        class DownloadCompleted(release: Release) : DownloadEvent(release)
    }
}
