package com.cloudfiveapp.android.ui.releaseslist

import com.cloudfiveapp.android.data.ProductId
import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.data.remote.ReleasesApi
import com.cloudfiveapp.android.util.extensions.toResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ReleasesRepository
@Inject constructor(@Named("mock") private val releasesApi: ReleasesApi,
                    private val errorConverter: ApiErrorConverter)
    : ReleasesListContract.Repository {

    override val releasesOutcome: PublishSubject<Outcome<List<Release>>> = PublishSubject.create()

    override fun refreshReleases(productId: ProductId) {
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
                        onError = { error ->
                            releasesOutcome.onNext(Outcome.loading(false))
                            releasesOutcome.onNext(Outcome.error(error))
                        })
    }
}
