package com.example.crudgraduation.Fragments;

import static android.content.Context.MODE_PRIVATE;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.example.crudgraduation.Activity.HelpActivity;
import com.example.crudgraduation.Activity.MainActivity;
import com.example.crudgraduation.R;
import java.util.Locale;
import java.util.Objects;


public class SettingssFragment extends Fragment {

    public final static String TAG;

    static {
        TAG = SettingssFragment.class.getCanonicalName();
    }

    Boolean languageChanged = false;
    ImageButton appDetails;
    RelativeLayout app_details, app_language,app_help;

    public SettingssFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadLocale();
        View rootView = inflater.inflate(R.layout.fragment_settingss, container, false);
        findViews(rootView);
        setUp();

        return rootView;
    }
    private void findViews(View rootView) {
        appDetails =(ImageButton) rootView.findViewById(R.id.button_details);
        app_details = rootView.findViewById(R.id.app_details);
        app_language = rootView.findViewById(R.id.app_language);
        app_help = rootView.findViewById(R.id.app_help);

    }

    public void setUp(){

        app_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show AlertDialog to display list of languages, one can be selected
                showChangeLanguageDialog();
            }
        });
        app_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to another fragment
                AboutAppFragment nextFrag= new AboutAppFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
        app_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open help activity
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
                System.out.println("HELP");
            }
        });


    }

    private void checkLanguage(Boolean languageChanged) {
        if (languageChanged == true){
            //go to mainActivity refresh
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void showChangeLanguageDialog() {
        //array of languages to display in alert dialog
        final String[] listItems = {"Македонски","English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext(),R.style.AlertDialogStyle);
        mBuilder.setTitle(R.string.lbl_select_language);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
                    //Macedonian
                    setLocale("mk");
                    checkLanguage(languageChanged);
                    //recreate();
                }
                else if (i==1){
                    //English
                    setLocale("en");
                    checkLanguage(languageChanged);
                    //recreate();
                }

                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().getDecorView().setBackgroundResource(R.drawable.button_transparent);
        mDialog.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config,getContext().getResources().getDisplayMetrics());
        //save data to shared preferences
        SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
        languageChanged = true;

    }

    //load language saved in shared preferences
    public void loadLocale(){
        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_lang","");
        setLocale(language);
    }


}