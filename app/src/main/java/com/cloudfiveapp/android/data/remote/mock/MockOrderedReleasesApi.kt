package com.cloudfiveapp.android.data.remote.mock

import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.data.remote.ReleasesApi
import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.mock.BehaviorDelegate

class MockOrderedReleasesApi(private val delegate: BehaviorDelegate<ReleasesApi>)
    : ReleasesApi {

    companion object {
        private const val REPO_NAME = "resident_app_android"
        private const val DOWNLOAD_URL = "https://www.cloudfiveapp.com/builds/13838/artifact"

        private var timesCalled = 0

        private val orderedReleases = listOf(
                listOf(
                        Release("1", "Tenforward Dev", "v3.1", "100", REPO_NAME, "abc123", DOWNLOAD_URL),
                        Release("2", "Tenforward QA", "v3.1", "150", REPO_NAME, "xyz987", DOWNLOAD_URL)),
                listOf(
                        Release("1", "Tenforward Dev", "v3.1", "101", REPO_NAME, "def456", DOWNLOAD_URL),
                        Release("2", "Tenforward QA", "v3.1", "150", REPO_NAME, "xyz987", DOWNLOAD_URL)),
                listOf(
                        Release("1", "Tenforward Dev", "v3.2", "110", REPO_NAME, "lmn046", DOWNLOAD_URL),
                        Release("3", "Draper", "v3.1", "216", REPO_NAME, "abc123", DOWNLOAD_URL),
                        Release("2", "Tenforward QA", "v3.1", "150", REPO_NAME, "xyz987", DOWNLOAD_URL)),
                emptyList())
    }

    override fun getReleases(productId: String): Single<Result<List<Release>>> {
        val currentCallIndex = timesCalled
        timesCalled = (timesCalled + 1) % orderedReleases.size
        return delegate.returningResponse(orderedReleases[currentCallIndex]).getReleases(productId)
    }
}
