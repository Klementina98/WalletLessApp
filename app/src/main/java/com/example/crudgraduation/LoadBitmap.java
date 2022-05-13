package com.example.crudgraduation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoadBitmap extends AsyncTask<String, Void, Bitmap> {


    @Override
    protected Bitmap doInBackground(String... urls) {
        return downloadImage(urls[0]);
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        try {
            URL url1 = new URL(url);
            URLConnection connection = url1.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();

        }catch (IOException e){
            Log.i("Hub","Error getting the image from server : "+ e.getMessage().toString());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result){
    }

}