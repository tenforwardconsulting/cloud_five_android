package com.cloudfiveapp.android.ui.productslist

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.cloudfiveapp.android.BaseTest
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.util.atPosition
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductsListActivityTest : BaseTest() {

    @Rule
    @JvmField
    val activityTestRule = IntentsTestRule<ProductsListActivity>(ProductsListActivity::class.java, true, false)

    @Test
    fun showsList() {
        activityTestRule.launchActivity(null)
        Thread.sleep(1500)
        onView(withId(R.id.productsRecycler)).apply {
            check(matches(atPosition(0,
                    hasDescendant(withText("Mobile Doorman")))))
            check(matches(atPosition(1,
                    hasDescendant(withText("Staff App")))))
            check(matches(atPosition(2,
                    hasDescendant(withText("Virtualbadge V2")))))
            check(matches(atPosition(3,
                    hasDescendant(withText("Cloud Five App")))))
        }
    }
}
