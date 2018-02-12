package com.cloudfiveapp.android.ui.appslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.application.CloudFiveApp
import com.cloudfiveapp.android.ui.appslist.data.App
import com.cloudfiveapp.android.ui.appslist.di.DaggerAppsListComponent
import com.cloudfiveapp.android.ui.appslist.model.AppsListContract
import com.cloudfiveapp.android.util.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_apps_list.*
import timber.log.Timber
import javax.inject.Inject

class AppsListActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AppsListActivity::class.java)
        }
    }

    private val component by lazy {
        DaggerAppsListComponent.builder().appComponent(CloudFiveApp.appComponent).build()
    }

    @Inject
    lateinit var appsRepository: AppsListContract.Repository

    @Inject
    lateinit var appsAdapter: AppsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps_list)
        component.inject(this)

        appsAdapter.interactor = object : AppsAdapter.AppInteractor {
            override fun onDownloadClicked(app: App) {
                toast("Download ${app.name}")
            }
        }
        appsRecycler.adapter = appsAdapter

        appsRepository.apps
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { apps ->
                            appsAdapter.setData(apps)
                            Timber.d("onNext")
                        },
                        onError = { throwable ->
                            throwable.printStackTrace()
                        })
    }
}
