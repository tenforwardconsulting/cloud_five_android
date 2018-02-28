package com.cloudfiveapp.android.ui.login

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import com.cloudfiveapp.android.BaseTest
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.util.hasTextInputLayoutError
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest : BaseTest() {

    @Rule
    @JvmField
    val activityTestRule = IntentsTestRule<LoginActivity>(LoginActivity::class.java, true, false)

    @Test
    fun loginSuccessTest() {
        activityTestRule.launchActivity(null)
        onView(withId(R.id.loginEmailInput)).perform(clearText(), replaceText("test@test.com"))
        onView(withId(R.id.loginPasswordInput)).perform(clearText(), replaceText("test"))
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(1000)
        assertTrue(activityTestRule.activity.isFinishing)
    }

    @Test
    fun loginFailedTest() {
        activityTestRule.launchActivity(null)
        onView(withId(R.id.loginEmailInput)).perform(clearText(), replaceText("test@test.com"))
        onView(withId(R.id.loginPasswordInput)).perform(clearText(), replaceText("wrong_password"))
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.loginEmailTextInputLayout)).check(matches(hasTextInputLayoutError("Invalid email/password combination")))
        onView(withId(R.id.loginPasswordTextInputLayout)).check(matches(hasTextInputLayoutError("Invalid email/password combination")))
    }
}
