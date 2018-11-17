package com.cloudfiveapp.android.ui.releaseslist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.data.model.Release
import com.cloudfiveapp.android.util.extensions.inflate

class ReleasesAdapter : ListAdapter<Release, ReleaseViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Release>() {

            override fun areItemsTheSame(oldItem: Release, newItem: Release): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Release, newItem: Release): Boolean {
                return oldItem == newItem
            }
        }
    }

    var interactor: ReleaseInteractor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseViewHolder {
        return ReleaseViewHolder(parent.inflate(R.layout.row_release))
    }

    override fun onBindViewHolder(holder: ReleaseViewHolder, position: Int) {
        holder.bind(getItem(position), interactor)
    }
}
