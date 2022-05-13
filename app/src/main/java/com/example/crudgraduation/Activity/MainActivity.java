package com.example.crudgraduation.Activity;


import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.ActivityManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.crudgraduation.Fragments.DashboardFragment;
import com.example.crudgraduation.Fragments.MerchantsFragment;
import com.example.crudgraduation.Fragments.SettingssFragment;
import com.example.crudgraduation.R;
import com.example.crudgraduation.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@VisibleForTesting
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        loadLocale();
        //for the first time, we want merchants fragment
        FirebaseApp.initializeApp(this);
        replaceFragment(new DashboardFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.dashboard:
                    replaceFragment(new DashboardFragment());
                    break;
                case R.id.merchants:
                    replaceFragment(new MerchantsFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingssFragment());
                    break;
            }
            return true;
        });
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }

    public void loadLocale(){
        SharedPreferences prefs = Objects.requireNonNull(this.getSharedPreferences("Settings", MODE_PRIVATE));
        String language = prefs.getString("My_lang","");
        setLocale(language);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());
        //save data to shared preferences
        SharedPreferences.Editor editor = Objects.requireNonNull(this.getSharedPreferences("Settings",MODE_PRIVATE).edit());
        editor.putString("My_lang", lang);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //strelkata nazad gore
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                hideUpButton();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBackPressed() { //sistemskoto
        super.onBackPressed();

        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        for (int i=0;i<taskList.size();i++){
           if( taskList.get(i).topActivity.getClassName().equals("com.example.crudgraduation.Activity.ScanActivity")){
               this.finishAffinity();
           }else{
               getSupportFragmentManager().popBackStack();
               hideUpButton();
           }
        }
    }

    public void showUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
    public void hideUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }

}