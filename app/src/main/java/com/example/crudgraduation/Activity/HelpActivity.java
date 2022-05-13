package com.example.crudgraduation.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.crudgraduation.Adapter.SliderAdapter;
import com.example.crudgraduation.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.Objects;

public class HelpActivity extends AppCompatActivity {

    SliderView slider;
    int[] images_mk = {R.drawable.home_mk, R.drawable.merchants_mk, R.drawable.addnew_mk, R.drawable.search_mk,
            R.drawable.barcode_mk, R.drawable.deletecard_mk, R.drawable.info_mk, R.drawable.localization_mk
    };

    int[] images_en = {R.drawable.home_en, R.drawable.merchants_en, R.drawable.addnew_en, R.drawable.search_en,
            R.drawable.barcode_en, R.drawable.deletecard_en, R.drawable.info_en, R.drawable.localization_en
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        SharedPreferences prefs = Objects.requireNonNull(getApplication().getSharedPreferences("Settings", MODE_PRIVATE));
        String language = prefs.getString("My_lang","");
        slider = findViewById(R.id.slider);
        SliderAdapter sliderAdapter;
        if (language.equals("en")){

            sliderAdapter = new SliderAdapter(images_en);
        }else{
            sliderAdapter = new SliderAdapter(images_mk);
        }
        slider.setSliderAdapter(sliderAdapter);
        setUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                    this.finish();
                    hideUpButton();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }

    @Override
    public void onBackPressed() { //sistemskoto
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
        hideUpButton();
    }

    private void setUp() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


}