package com.example.crudgraduation.Fragments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.openLinkWithText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.crudgraduation.R;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class AddMerchantFragmentTest extends TestCase {


    @Rule
    public FragmentTestRule<MerchantsFragment> newMerchantFragmentTestRule = new FragmentTestRule<>(MerchantsFragment.class);




    @Test
    public void testSendingRequestForNewMerchant(){
        newMerchantFragmentTestRule.launchActivity(null);
        onView(withId(R.id.add_merchant)).perform(click());

        onView(withId(R.id.merchantName)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Test Name Merchant"),closeSoftKeyboard());

        onView(withId(R.id.merchantDesc)).perform(ViewActions.clearText())
                .perform(ViewActions.typeText("Test Name Merchant"),closeSoftKeyboard());

        onView(withId(R.id.send_request)).perform(openLinkWithText("www.google.com"));


    }
}
