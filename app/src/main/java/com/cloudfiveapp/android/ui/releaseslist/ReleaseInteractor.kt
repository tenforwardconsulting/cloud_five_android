package com.cloudfiveapp.android.ui.releaseslist

import com.cloudfiveapp.android.data.model.Release

interface ReleaseInteractor {
    fun onDownloadClicked(release: Release)
}
