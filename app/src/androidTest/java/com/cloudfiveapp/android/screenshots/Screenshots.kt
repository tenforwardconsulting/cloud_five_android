package com.cloudfiveapp.android.screenshots

import android.support.test.runner.AndroidJUnit4
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.cloudfiveapp.android.ui.login.LoginActivity
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

@RunWith(AndroidJUnit4::class)
class ScreenshotsTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUp() {
            Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        }
    }

    @Rule
    @JvmField
    val intentsTestRule = IntentsTestRule<LoginActivity>(LoginActivity::class.java, true, false)

    @Rule
    @JvmField
    val failedRule = object : TestWatcher() {
        override fun failed(e: Throwable?, description: Description?) {
            Screengrab.screenshot("error")

            super.failed(e, description)
        }
    }

    @Test
    fun takeScreenshots() {
        intentsTestRule.launchActivity(null)

        Thread.sleep(5000)
        Screengrab.screenshot("1-login")
    }
}