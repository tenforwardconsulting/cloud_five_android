package com.cloudfiveapp.android.util

import android.support.design.widget.TextInputLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasTextInputLayoutError(expectedErrorText: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) = Unit

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextInputLayout) {
                return false
            }
            return item.error == expectedErrorText
        }
    }
}

fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(item: RecyclerView): Boolean {
            val viewHolder = item.findViewHolderForAdapterPosition(position)
            return if (viewHolder != null) {
                itemMatcher.matches(viewHolder.itemView)
            } else {
                false
            }
        }
    }
}
