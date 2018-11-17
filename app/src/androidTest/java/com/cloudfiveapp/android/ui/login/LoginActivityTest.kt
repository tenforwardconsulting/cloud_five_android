package com.cloudfiveapp.android.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.cloudfiveapp.android.BaseTest
import com.cloudfiveapp.android.R
import com.cloudfiveapp.android.util.hasTextInputLayoutError
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.not
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
        onView(withId(R.id.loginEmailTextInputLayout)).check(matches(not(isEnabled())))
        onView(withId(R.id.loginPasswordTextInputLayout)).check(matches(not(isEnabled())))
        Thread.sleep(1500)
        assertTrue(activityTestRule.activity.isFinishing)
    }

    @Test
    fun loginFailedTest() {
        activityTestRule.launchActivity(null)
        onView(withId(R.id.loginEmailInput)).perform(clearText(), replaceText("test@test.com"))
        onView(withId(R.id.loginPasswordInput)).perform(clearText(), replaceText("wrong_password"))
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.loginEmailTextInputLayout)).check(matches(not(isEnabled())))
        onView(withId(R.id.loginPasswordTextInputLayout)).check(matches(not(isEnabled())))
        Thread.sleep(1500)
        onView(withId(R.id.loginEmailTextInputLayout)).check(matches(hasTextInputLayoutError("Invalid email/password combination")))
        onView(withId(R.id.loginPasswordTextInputLayout)).check(matches(hasTextInputLayoutError("Invalid email/password combination")))

        onView(withId(R.id.loginEmailTextInputLayout)).check(matches(isEnabled()))
        onView(withId(R.id.loginPasswordTextInputLayout)).check(matches(isEnabled()))
    }
}
