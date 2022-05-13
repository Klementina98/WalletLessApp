package com.example.crudgraduation.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.crudgraduation.CaptureAct;
import com.example.crudgraduation.DbBitmapUtility;
import com.example.crudgraduation.Model.GeoLocation;
import com.example.crudgraduation.LoadBitmap;
import com.example.crudgraduation.MyDatabaseHelper;
import com.example.crudgraduation.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ScanActivity extends AppCompatActivity {
    public final static String TAG;

    static {
        TAG = ScanActivity.class.getCanonicalName();
    }

    ImageView barcodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        barcodeImage = findViewById(R.id.barcodeImage);
        scanCode();
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning code");
        integrator.initiateScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        Intent intent = getIntent();
        if(result !=null){
            String barcode = result.getContents();
            String cardName = intent.getStringExtra("cardName");
            String cardImage = intent.getStringExtra("cardImage");
            String typeOfCode = result.getFormatName();
            ArrayList<GeoLocation> locations = intent.getParcelableArrayListExtra("locations");
            if (result.getContents()!=null){
                //lets save image from url
                try {
                    saveBitmapAndConvert(cardName,barcode,cardImage,locations,typeOfCode);
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else {
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                Toast.makeText(this,"No results",Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode,data);
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveBitmapAndConvert(String cardName, String barcode, String cardImage, ArrayList<GeoLocation> locations,String typeOfCode) throws IOException, ExecutionException, InterruptedException {
        //make bitmap from URL
        Bitmap bitmap = new LoadBitmap().execute(cardImage).get();
        byte[] bytesOfImage= DbBitmapUtility.getBytes(bitmap); //first convert the bitmap to byte[]
        saveCardWithLocationsIntoDataBase(cardName,barcode,bytesOfImage,locations,typeOfCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveCardWithLocationsIntoDataBase(String cardName, String barcode, byte[] bytesOfImage, ArrayList<GeoLocation> locations,String typeOfCode) {
        MyDatabaseHelper myDB = new MyDatabaseHelper((ScanActivity.this));
        //check if the card is already added
        boolean exist = myDB.existCard(cardName);
        if (!exist){
            myDB.insertCard(cardName,barcode,bytesOfImage,locations,typeOfCode); //koga insertime kartichka, treba da i gi insertirame i lokacite
        }else{
            //we already have that card
            Toast.makeText(this,"You already have this card in your wallet",Toast.LENGTH_SHORT).show();
        }
        //back to dashboard screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() { //sistemskoto
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        hideUpButton();
    }

    public void hideUpButton() { getSupportActionBar().setDisplayHomeAsUpEnabled(false); }
}