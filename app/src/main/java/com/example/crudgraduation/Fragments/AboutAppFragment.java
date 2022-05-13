
package com.example.crudgraduation.Fragments;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.crudgraduation.Activity.MainActivity;
import com.example.crudgraduation.R;

public class AboutAppFragment extends Fragment implements FragmentManager.OnBackStackChangedListener{

    public final static String TAG;

    static {
        TAG = AboutAppFragment.class.getCanonicalName();
    }

    public AboutAppFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    public void onBackStackChanged() {
        if(getActivity() != null) {
            // enable Up button only if there are entries on the backstack
            if(getActivity().getSupportFragmentManager().getBackStackEntryCount() < 1) {
                ((MainActivity)getActivity()).hideUpButton();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about_app, container, false);
        setUp();
        return rootView;
    }
    private void setUp() {
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}