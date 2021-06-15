package com.example.banking_app


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(Login::class.java)

    @Test
    fun loginTest() {
        val appCompatEditText = onView(
            allOf(withId(R.id.email),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        appCompatEditText.perform(replaceText("test@test.com"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    2),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("1234567890"), closeSoftKeyboard())

        val materialCheckBox = onView(
            allOf(withId(R.id.cbShowPwd), withText("Show password"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    3),
                isDisplayed()))
        materialCheckBox.perform(click())

        val materialButton = onView(
            allOf(withId(R.id.button), withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    0),
                isDisplayed()))
        materialButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
