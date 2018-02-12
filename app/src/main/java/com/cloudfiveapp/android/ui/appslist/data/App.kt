package com.cloudfiveapp.android.ui.appslist.data

data class App(val name: String,
               val version: String,
               val latestBuildNumber: String,
               val repoName: String,
               val commitHash: String)
