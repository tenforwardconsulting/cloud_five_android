package com.cloudfiveapp.android.ui.appslist

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.ui.appslist.data.App
import com.cloudfiveapp.android.util.inflate
import kotlinx.android.synthetic.main.row_app.view.*

class AppsAdapter : RecyclerView.Adapter<AppsAdapter.AppViewHolder>() {

    private var apps: List<App> = emptyList()

    var interactor: AppInteractor? = null

    override fun getItemCount() = apps.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(parent.inflate(R.layout.row_app))
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        holder.bind(app)
        with(holder.itemView) {
            rowAppDownloadButtonContainer.setOnClickListener {
                interactor?.onDownloadClicked(app)
            }
        }
    }

    fun setData(list: List<App>) {
        apps = list
        notifyDataSetChanged()
        // TODO: diff util?
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(app: App) {
            with(itemView) {
                rowAppName.text = app.name
                rowAppVersion.text = app.version
                rowAppBuildNumber.text = context.getString(R.string.build_number_formatted, app.latestBuildNumber)
                rowAppRepoName.text = app.repoName
                rowAppCommitHash.text = app.commitHash
            }
        }
    }

    interface AppInteractor {
        fun onDownloadClicked(app: App)
    }
}
