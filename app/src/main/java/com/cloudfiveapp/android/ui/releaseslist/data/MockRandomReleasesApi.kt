package com.cloudfiveapp.android.ui.releaseslist.data

import io.reactivex.Single
import java.security.MessageDigest
import java.util.*

class MockRandomReleasesApi : ReleasesApi {

    companion object {
        private const val REPO_NAME = "resident_app_android"
        private const val MAX_RELEASES = 75
    }

    private val random = Random(123L)

    override fun getReleases(productId: String): Single<List<Release>> {
        val n = random.nextInt(MAX_RELEASES)
        val releases = mutableListOf<Release>()
        (0 until n).forEach {
            releases += Release(randomId(), randomName(), randomVersion(), randomBuildNumber(), REPO_NAME, randomCommitHash())
        }
        return Single.just(releases)
    }

    private fun randomId(): String {
        return random.nextInt(1000).toString()
    }

    private fun randomName(): String {
        return "Ten Forward ${random.nextInt(100)}"
    }

    private fun randomVersion(): String {
        return "v3.${random.nextInt(50)}"
    }

    private fun randomBuildNumber(): String {
        return random.nextInt(8).toString()
    }

    private fun randomCommitHash(): String {
        val crypt = MessageDigest.getInstance("SHA-1")
        crypt.reset()
        val bytes = ByteArray(64)
        random.nextBytes(bytes)
        crypt.update(bytes)
        return byteToHex(crypt.digest()).substring(0, 8)
    }

    private fun byteToHex(bytes: ByteArray): String {
        return Formatter().use { formatter ->
            bytes.forEach { formatter.format("%02x", it) }
            formatter.toString()
        }
    }
}
