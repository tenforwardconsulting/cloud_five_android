package com.cloudfiveapp.android.ui.releaseslist

import androidx.lifecycle.MutableLiveData
import com.cloudfiveapp.android.data.ApiErrorConverter
import com.cloudfiveapp.android.data.ProductId
import com.cloudfiveapp.android.data.model.Outcome
import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.data.remote.ReleasesApi
import com.cloudfiveapp.android.util.extensions.enqueue
import com.cloudfiveapp.android.util.extensions.toOutcome
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ReleasesRepository
@Inject constructor(@Named("mock") private val releasesApi: ReleasesApi,
                    private val errorConverter: ApiErrorConverter)
    : ReleasesListContract.Repository {

    override val releasesOutcome = MutableLiveData<Outcome<List<Release>>>()

    override fun refreshReleases(productId: ProductId) {
        releasesOutcome.value = Outcome.loading(true)
        releasesApi.getReleases(productId)
                .enqueue(
                        success = { response ->
                            releasesOutcome.value = Outcome.loading(false)
                            releasesOutcome.value = response.toOutcome(errorConverter)
                        },
                        failure = {
                            releasesOutcome.value = Outcome.loading(false)
                            releasesOutcome.value = Outcome.error(it)
                        })
    }
}
