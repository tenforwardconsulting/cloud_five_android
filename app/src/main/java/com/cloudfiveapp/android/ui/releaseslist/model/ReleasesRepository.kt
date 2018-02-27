package com.cloudfiveapp.android.ui.releaseslist.model

import com.cloudfiveapp.android.ui.common.networking.ApiErrorConverter
import com.cloudfiveapp.android.ui.common.networking.Outcome
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.data.ReleasesApi
import com.cloudfiveapp.android.util.extensions.toResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ReleasesRepository(private val releasesApi: ReleasesApi,
                         private val errorConverter: ApiErrorConverter,
                         private val compositeDisposable: CompositeDisposable)
    : ReleasesListContract.Repository {

    override val releasesOutcome: PublishSubject<Outcome<List<Release>>> = PublishSubject.create()

    override fun refreshReleasesFor(productId: String) {
        releasesOutcome.onNext(Outcome.loading(true))
        releasesApi.getReleases(productId)
                .map { it.toResult(errorConverter) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            releasesOutcome.onNext(Outcome.loading(false))
                            releasesOutcome.onNext(it)
                        },
                        onError = {
                            releasesOutcome.onNext(Outcome.loading(false))
                            releasesOutcome.onNext(Outcome.error(it))
                        })
                .addTo(compositeDisposable)
    }
}
