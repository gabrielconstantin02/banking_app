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
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ChangePasswordTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(Login::class.java)

    @Test
    fun changePasswordTest() {
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

        val materialButton = onView(
            allOf(withId(R.id.button), withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    0),
                isDisplayed()))
        materialButton.perform(click())

        val bottomNavigationItemView = onView(
            allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationView),
                        0),
                    2),
                isDisplayed()))
        bottomNavigationItemView.perform(click())

        val materialButton2 = onView(
            allOf(withId(R.id.settingsButton), withText("Change password"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1),
                    0),
                isDisplayed()))
        materialButton2.perform(click())

        val editText = onView(
            allOf(withId(R.id.pwd1),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        editText.perform(replaceText("123456"), closeSoftKeyboard())

        val editText2 = onView(
            allOf(withId(R.id.pwd2),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    2),
                isDisplayed()))
        editText2.perform(replaceText("123456"), closeSoftKeyboard())

        val button = onView(
            allOf(withId(R.id.button), withText("Change Password"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    4),
                isDisplayed()))
        button.perform(click())

        val materialButton3 = onView(
            allOf(withId(R.id.settingsButton), withText("Change password"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1),
                    0),
                isDisplayed()))
        materialButton3.perform(click())

        val editText3 = onView(
            allOf(withId(R.id.pwd1),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        editText3.perform(replaceText("1234567890"), closeSoftKeyboard())

        val editText4 = onView(
            allOf(withId(R.id.pwd2),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    2),
                isDisplayed()))
        editText4.perform(replaceText("1234567890"), closeSoftKeyboard())

        val button2 = onView(
            allOf(withId(R.id.button), withText("Change Password"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    4),
                isDisplayed()))
        button2.perform(click())
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
