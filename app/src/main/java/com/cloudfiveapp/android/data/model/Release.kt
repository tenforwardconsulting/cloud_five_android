package com.cloudfiveapp.android.data.model

data class Release(val id: String,
                   val name: String,
                   val version: String,
                   val latestBuildNumber: String,
                   val repoName: String,
                   val commitHash: String,
                   val downloadUrl: String) {

    val downloadFileName: String
        get() = "${name.replace(' ', '_')}-$version-$latestBuildNumber.apk"
}
