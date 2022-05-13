package com.example.crudgraduation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.crudgraduation.R;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class SplashActivty extends AppCompatActivity {

    private static final String LOG_TAG = "HELlO";
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activty);
        hasActiveInternetConnection(this);
        image = findViewById(R.id.gif);

        Glide.with(this).asGif().load(R.raw.splashgif).
    apply(new RequestOptions().override(800, 800)).into(image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivty.this,MainActivity.class));
                finish();
            }
        },5000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    public  boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error checking internet connection", e);

            }
        } else {
            System.out.println("No network available!");
        }
        return false;
    }
}