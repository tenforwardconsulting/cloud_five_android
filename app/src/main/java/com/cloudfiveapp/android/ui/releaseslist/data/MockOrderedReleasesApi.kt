package com.cloudfiveapp.android.ui.releaseslist.data

import io.reactivex.Single

class MockOrderedReleasesApi : ReleasesApi {

    companion object {
        private const val REPO_NAME = "resident_app_android"
        private var timesCalled = 0

        private val orderedReleases = listOf(
                listOf(
                        Release("1", "Tenforward Dev", "v3.1", "100", REPO_NAME, "abc123"),
                        Release("2", "Tenforward QA", "v3.1", "150", REPO_NAME, "xyz987")),
                listOf(
                        Release("1", "Tenforward Dev", "v3.1", "101", REPO_NAME, "def456"),
                        Release("2", "Tenforward QA", "v3.1", "150", REPO_NAME, "xyz987")),
                listOf(
                        Release("1", "Tenforward Dev", "v3.2", "110", REPO_NAME, "lmn046"),
                        Release("3", "Draper", "v3.1", "216", REPO_NAME, "abc123"),
                        Release("2", "Tenforward QA", "v3.1", "150", REPO_NAME, "xyz987")))
    }

    override fun getReleases(productId: String): Single<List<Release>> {
        val currentCallIndex = timesCalled
        timesCalled = (timesCalled + 1) % orderedReleases.size
        return Single.just(orderedReleases[currentCallIndex])
    }
}
