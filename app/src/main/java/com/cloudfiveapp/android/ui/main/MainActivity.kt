package com.cloudfiveapp.android.ui.main

import android.os.Bundle
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.application.BaseActivity
import com.cloudfiveapp.android.ui.login.LoginActivity
import com.cloudfiveapp.android.ui.productslist.ProductsListActivity
import com.cloudfiveapp.android.ui.releaseslist.ReleasesListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLoginButton.setOnClickListener {
            startActivity(LoginActivity.newIntent(this))
        }

        mainProductsButton.setOnClickListener {
            startActivity(ProductsListActivity.newIntent(this))
        }

        mainReleasesButton.setOnClickListener {
            startActivity(ReleasesListActivity.newIntent(this, "1"))
        }
    }
}
