package com.cloudfiveapp.android.ui.releaseslist.viewmodel

import android.arch.lifecycle.ViewModel
import com.cloudfiveapp.android.ui.common.data.ProductId
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.model.ApkDownloader
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class ReleasesListViewModel(private val releasesRepository: ReleasesListContract.Repository,
                            val apkDownloader: ApkDownloader)
    : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val productIdSubject = BehaviorSubject.create<ProductId>()

    private val refreshingSubject = PublishSubject.create<Boolean>()
    val refreshing: Observable<Boolean>
        get() = refreshingSubject

    val releases: Observable<List<Release>> = productIdSubject
            .distinct()
            .doOnNext { refreshingSubject.onNext(true) }
            .flatMap { productId ->
                releasesRepository.getReleases(productId)
            }
            .doOnNext {
                refreshingSubject.onNext(false)
            }
            .subscribeOn(Schedulers.io())
            .replay(1).autoConnect()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun refreshReleases() {
        refreshingSubject.onNext(true)
        releasesRepository.refresh()
    }

    fun setProductId(productId: ProductId) {
        productIdSubject.onNext(productId)
    }
}
