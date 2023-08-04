package com.cloudfiveapp.android.ui.releaseslist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.databinding.RowReleaseBinding

class ReleaseViewHolder(private val itemBinding: RowReleaseBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(release: Release, releaseInteractor: ReleaseInteractor?) {
        with(itemView) {
            itemBinding.rowReleaseName.text = release.name
            itemBinding.rowReleaseVersion.text = release.version
            itemBinding.rowReleaseBuildNumber.text = context.getString(R.string.build_number_formatted, release.latestBuildNumber)
            itemBinding.rowReleaseRepoName.text = release.repoName
            itemBinding.rowReleaseCommitHash.text = release.commitHash
            itemBinding.rowReleaseDownloadButtonContainer.setOnClickListener {
                releaseInteractor?.onDownloadClicked(release)
            }
        }
    }
}
