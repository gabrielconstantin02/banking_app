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
import java.lang.StringBuilder
import kotlin.random.Random

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(Login::class.java)

    @Test
    fun registerTest() {
        val cnp: String = generateCNP()
        val materialButton = onView(
            allOf(withId(R.id.button2), withText("Signup"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    4),
                isDisplayed()))
        materialButton.perform(click())

        val appCompatEditText = onView(
            allOf(withId(R.id.first_name),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    2),
                isDisplayed()))
        appCompatEditText.perform(replaceText("tester1" + cnp.substring(0,5)), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(withId(R.id.last_name),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    3),
                isDisplayed()))
        appCompatEditText2.perform(replaceText("tester1" + cnp.substring(6)), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(withId(R.id.email),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    1),
                isDisplayed()))
        appCompatEditText3.perform(replaceText("test$cnp@tester1.com"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(withId(R.id.cnp),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    0),
                isDisplayed()))
        appCompatEditText4.perform(replaceText(cnp), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(withId(R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    5),
                isDisplayed()))
        appCompatEditText5.perform(replaceText("123456"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(withId(R.id.password2),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    4),
                isDisplayed()))
        appCompatEditText6.perform(replaceText("123456"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(withId(R.id.button), withText("Create Account"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0),
                    6),
                isDisplayed()))
        materialButton2.perform(click())
    }

    private fun generateCNP(): String {
        val cnp = StringBuilder("")
        for (i in 1..13) {
            val digit: Int = Random.nextInt(10)
            cnp.append(digit.toString())
        }
        return cnp.toString()
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
