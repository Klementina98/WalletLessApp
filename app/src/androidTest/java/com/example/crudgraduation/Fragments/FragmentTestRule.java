package com.example.crudgraduation.Fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.rule.ActivityTestRule;

import com.example.crudgraduation.Activity.MainActivity;
import com.example.crudgraduation.R;

import org.junit.Assert;

public class FragmentTestRule <F extends Fragment> extends ActivityTestRule<MainActivity> {

    private final Class<F> mFragmentClass;
    private F mFragment;


    public FragmentTestRule(Class<F> activityClass) {
        super(MainActivity.class, true, false);
        mFragmentClass = activityClass;
    }


    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(() -> {
            try {
                //Instantiate and insert the fragment into the container layout
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                mFragment = mFragmentClass.newInstance();
                transaction.replace(R.id.container, mFragment);
                transaction.commit();
            } catch (InstantiationException | IllegalAccessException e) {
                Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                        getClass().getSimpleName(),
                        mFragmentClass.getSimpleName(),
                        e.getMessage()));
            }
        });
    }

    public F getmFragment(){
        return mFragment;
    }
}
