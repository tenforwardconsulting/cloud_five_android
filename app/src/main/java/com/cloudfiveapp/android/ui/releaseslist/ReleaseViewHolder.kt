package com.cloudfiveapp.android.ui.releaseslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.data.model.Release
import kotlinx.android.synthetic.main.row_release.view.*

class ReleaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(release: Release, releaseInteractor: ReleaseInteractor?) {
        with(itemView) {
            rowReleaseName.text = release.name
            rowReleaseVersion.text = release.version
            rowReleaseBuildNumber.text = context.getString(R.string.build_number_formatted, release.latestBuildNumber)
            rowReleaseRepoName.text = release.repoName
            rowReleaseCommitHash.text = release.commitHash
            rowReleaseDownloadButtonContainer.setOnClickListener {
                releaseInteractor?.onDownloadClicked(release)
            }
        }
    }
}
