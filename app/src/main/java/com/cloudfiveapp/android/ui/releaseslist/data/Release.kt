package com.cloudfiveapp.android.ui.releaseslist.data

data class Release(val name: String,
                   val version: String,
                   val latestBuildNumber: String,
                   val repoName: String,
                   val commitHash: String)
