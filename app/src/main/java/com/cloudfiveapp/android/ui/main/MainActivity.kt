package com.cloudfiveapp.android.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.databinding.ActivityLoginBinding
import com.cloudfiveapp.android.databinding.ActivityMainBinding
import com.cloudfiveapp.android.ui.login.LoginActivity
import com.cloudfiveapp.android.ui.releaseslist.ReleaseDownloadBroadcastReceiver

class MainActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val downloadManager: DownloadManager by lazy {
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val releaseDownloadBroadcastReceiver: ReleaseDownloadBroadcastReceiver by lazy {
        ReleaseDownloadBroadcastReceiver(binding.mainRootView, this, downloadManager)
    }

    private lateinit var navFragment: NavHostFragment
    private val navController by lazy { navFragment.navController }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        SessionManager.getSession().observe(this, Observer { session ->
            if (session is Unauthenticated) {
                finish()
                startActivity(LoginActivity.newIntent(this))
            }
        })

        binding.mainToolbar.inflateMenu(R.menu.main)

        binding.mainToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.mainLogOut) {
                SessionManager.logOut()
            }
            true
        }

        binding.mainToolbar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.mainToolbar.menu.findItem(R.id.mainLogOut).isVisible =
                    destination.id == R.id.productsListFragment
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(releaseDownloadBroadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(releaseDownloadBroadcastReceiver)
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()
}
