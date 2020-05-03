package edu.quinnipiac.ser210.wordcrunch;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GameActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void gameActivityTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.action_play), withText("Play Now!"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.frameLayout6),
                        childAtPosition(
                                allOf(withId(R.id.nav_host_fragment),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.word),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                2),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.missing_letter),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("e"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.missing_letter), withText("e"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.go_button), withText("GO"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView3),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                7),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.new_word_button), withText("New Word"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.word),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                2),
                        isDisplayed()));
        textView5.check(matches(isDisplayed()));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.back_button), withText("Back"),
                        childAtPosition(
                                allOf(withId(R.id.frameLayout6),
                                        childAtPosition(
                                                withId(R.id.nav_host_fragment),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction viewGroup2 = onView(
                allOf(withId(R.id.frameLayout),
                        childAtPosition(
                                allOf(withId(R.id.nav_host_fragment),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        viewGroup2.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
