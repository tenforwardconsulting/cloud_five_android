package com.cloudfiveapp.android.ui.releaseslist.adapter

import com.cloudfiveapp.android.ui.releaseslist.data.Release

interface ReleaseInteractor {
    fun onDownloadClicked(release: Release)
}
