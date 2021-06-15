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
class NavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(Login::class.java)

    @Test
    fun navigationTest() {
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

        val bottomNavigationItemView = onView(
            allOf(withId(R.id.navigation_payments), withContentDescription("Payments"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationView),
                        0),
                    1),
                isDisplayed()))
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationView),
                        0),
                    2),
                isDisplayed()))
        bottomNavigationItemView2.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(withId(R.id.navigation_overview), withContentDescription("Overview"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationView),
                        0),
                    0),
                isDisplayed()))
        bottomNavigationItemView3.perform(click())

        val relativeLayout = onView(
            allOf(withId(R.id.accountLogItem),
                childAtPosition(
                    allOf(withId(R.id.logDataHolder),
                        childAtPosition(
                            withId(R.id.logHolder),
                            1)),
                    0),
                isDisplayed()))
        relativeLayout.perform(click())

        val tabView = onView(
            allOf(withContentDescription("Cards"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0),
                    1),
                isDisplayed()))
        tabView.perform(click())

        val tabView2 = onView(
            allOf(withContentDescription("Details"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0),
                    2),
                isDisplayed()))
        tabView2.perform(click())

        val appCompatImageButton = onView(
            allOf(childAtPosition(
                allOf(withId(R.id.toolbarAccountDetails),
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0)),
                1),
                isDisplayed()))
        appCompatImageButton.perform(click())

        val bottomNavigationItemView4 = onView(
            allOf(withId(R.id.navigation_payments), withContentDescription("Payments"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationView),
                        0),
                    1),
                isDisplayed()))
        bottomNavigationItemView4.perform(click())

        val materialButton2 = onView(
            allOf(withId(R.id.buttonPayment), withText("Add"),
                childAtPosition(
                    allOf(withId(R.id.paymentmain),
                        childAtPosition(
                            withId(R.id.container),
                            0)),
                    1),
                isDisplayed()))
        materialButton2.perform(click())

        val materialButton3 = onView(
            allOf(withId(R.id.btnClose), withText("Close"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        materialButton3.perform(click())

        val bottomNavigationItemView5 = onView(
            allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationView),
                        0),
                    2),
                isDisplayed()))
        bottomNavigationItemView5.perform(click())

        val materialButton4 = onView(
            allOf(withId(R.id.aboutButton), withText("About"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        2),
                    0),
                isDisplayed()))
        materialButton4.perform(click())

        val button = onView(
            allOf(withId(R.id.btnClose), withText("Close"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    0),
                isDisplayed()))
        button.perform(click())
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
