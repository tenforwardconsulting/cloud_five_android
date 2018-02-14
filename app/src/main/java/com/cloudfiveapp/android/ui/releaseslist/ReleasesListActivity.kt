package com.cloudfiveapp.android.ui.releaseslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.ui.releaseslist.di.DaggerReleasesListComponent
import com.cloudfiveapp.android.ui.releaseslist.model.ReleasesListContract
import com.cloudfiveapp.android.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_releases_list.*
import timber.log.Timber
import javax.inject.Inject

class ReleasesListActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ReleasesListActivity::class.java)
        }
    }

    private val component by lazy {
        DaggerReleasesListComponent.builder().appComponent(CloudFiveApp.appComponent).build()
    }

    @Inject
    lateinit var releasesRepository: ReleasesListContract.Repository

    @Inject
    lateinit var releasesAdapter: ReleasesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_releases_list)
        component.inject(this)

        releasesAdapter.interactor = object : ReleasesAdapter.ReleaseInteractor {
            override fun onDownloadClicked(release: Release) {
                toast("Download ${release.name}")
            }
        }
        releasesRecycler.adapter = releasesAdapter

        releasesRepository.releases
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { apps ->
                            releasesAdapter.setData(apps)
                            Timber.d("onNext")
                        },
                        onError = { throwable ->
                            throwable.printStackTrace()
                        })
    }
}
