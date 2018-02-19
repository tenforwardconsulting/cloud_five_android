package com.cloudfiveapp.android.ui.releaseslist

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.ui.releaseslist.data.Release
import com.cloudfiveapp.android.util.extensions.inflate
import kotlinx.android.synthetic.main.row_release.view.*

class ReleasesAdapter : RecyclerView.Adapter<ReleasesAdapter.ReleaseViewHolder>() {

    private var releases: List<Release> = emptyList()

    var interactor: ReleaseInteractor? = null

    override fun getItemCount() = releases.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseViewHolder {
        return ReleaseViewHolder(parent.inflate(R.layout.row_release))
    }

    override fun onBindViewHolder(holder: ReleaseViewHolder, position: Int) {
        val release = releases[position]
        holder.bind(release)
        with(holder.itemView) {
            rowReleaseDownloadButtonContainer.setOnClickListener {
                interactor?.onDownloadClicked(release)
            }
        }
    }

    fun setData(list: List<Release>) {
        val diffCallback = ReleasesDiffCallback(releases, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        releases = list
        diffResult.dispatchUpdatesTo(this)
    }

    class ReleaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(release: Release) {
            with(itemView) {
                rowReleaseName.text = release.name
                rowReleaseVersion.text = release.version
                rowReleaseBuildNumber.text = context.getString(R.string.build_number_formatted, release.latestBuildNumber)
                rowReleaseRepoName.text = release.repoName
                rowReleaseCommitHash.text = release.commitHash
            }
        }
    }

    interface ReleaseInteractor {
        fun onDownloadClicked(release: Release)
    }

    private class ReleasesDiffCallback(val oldList: List<Release>, val newList: List<Release>)
        : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
