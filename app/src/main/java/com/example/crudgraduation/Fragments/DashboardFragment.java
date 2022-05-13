package com.example.crudgraduation.Fragments;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.crudgraduation.Adapter.CustomAdapter;
import com.example.crudgraduation.Model.Card;
import com.example.crudgraduation.Model.GeoLocation;
import com.example.crudgraduation.MyDatabaseHelper;
import com.example.crudgraduation.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardFragment extends Fragment implements CustomAdapter.onCardListener, LocationListener {

    ImageView barcodeImage;
    ImageView imageScanner;
    RecyclerView scannedCards;
    MyDatabaseHelper myDB;
    CustomAdapter customAdapter;
    ArrayList<Card> list;
    LocationManager locationManager;
    ArrayList<Card> sortedCards;
    ArrayList<GeoLocation> scannedLocations;
    boolean popUpFlag = false;

    FusedLocationProviderClient fusedLocationProviderClient;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        findViews(rootView);
        setUp();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUp() {
        sortedCards = new ArrayList<>();
        scannedLocations = new ArrayList<>();
        list = new ArrayList<>();
        myDB = new MyDatabaseHelper(getContext());
        customAdapter = new CustomAdapter(getContext(), list, this);
        scannedCards.setAdapter(customAdapter);
        scannedCards.setLayoutManager(new LinearLayoutManager(getContext()));

        deleteCardListener();
        storeDataInArrays();
        storeGeolocationsInArrays();
        getCurrentLocation();
    }

    //crate function to fetch data from the database to this arrays
    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) { //means there is no data
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
            imageScanner.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                Card card = new Card(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3),cursor.getString(4));
                list.add(card);
            }
        }
    }
    //create function to fetch data from database to ArrayList<Geolocation>
    @RequiresApi(api = Build.VERSION_CODES.N)
    void storeGeolocationsInArrays() {
        Cursor cursor = myDB.readAllDataFromLocations();
        if (cursor.getCount() == 0) { //means there is no data
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                GeoLocation location = new GeoLocation(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
                scannedLocations.add(location);
            }
        }
    }

    private void deleteCardListener() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                confirmDialog(viewHolder);
            }
        }).attachToRecyclerView(scannedCards);

    }

    private void confirmDialog(RecyclerView.ViewHolder viewHolder) {
        //display alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogStyle);
        builder.setTitle(R.string.delete);
        String deleteString = getResources().getString(R.string.delete_Card);
        String yes = getResources().getString(R.string.yes);
        builder.setMessage(Html.fromHtml("<font color='#F5F5F5'>"+deleteString+"</font>"));
        builder.setPositiveButton(Html.fromHtml("<font color='#F5F5F5'>"+yes+"</font>"), new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myDB.deleteCard((String) viewHolder.itemView.getTag());
                int position = viewHolder.getAdapterPosition();
                Card card = list.get(position);

                if (sortedCards.size()>0){
                    sortedCards.remove(position);
                    sortedCards.remove(card);
                }else{
                    list.remove(position);
                    list.remove(card);
                }
                List<GeoLocation> locationsToBeRemoved = scannedLocations.stream().filter(geoLocation -> geoLocation.getCard_Id().equals(card.getCardName())).collect(Collectors.toList());
                for (GeoLocation g : locationsToBeRemoved){
                    myDB.deleteGeoLocation(g.getCard_Id());
                    scannedLocations.remove(g);
                }
                customAdapter.notifyDataSetChanged();
                Cursor cursor = myDB.readAllData();
                if (cursor.getCount() == 0) { //means there is no data
                    Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
                    imageScanner.setVisibility(View.VISIBLE);
                }

            }
        });
        String no = getResources().getString(R.string.no);
        builder.setNegativeButton(Html.fromHtml("<font color='#F5F5F5'>"+no+"</font>"), new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //don't do anything
                customAdapter.notifyDataSetChanged();
            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().getDecorView().setBackgroundResource(R.drawable.button_transparent);
        mDialog.show();
    }

    private void findViews(View rootView) {
        barcodeImage = rootView.findViewById(R.id.barcodeImage);
        scannedCards = rootView.findViewById(R.id.scannedCards);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        imageScanner = rootView.findViewById(R.id.scanImage);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentLocation() {
        try {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCardClick(int position) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        System.out.println("!!!!"+list.get(position).getTypeOfCode());
        BitMatrix bitMatrix;
        try {
            if (list.get(position).getTypeOfCode().equals("QR_CODE")){
                 bitMatrix = multiFormatWriter.encode(list.get(position).getBarCode(), BarcodeFormat.QR_CODE, barcodeImage.getWidth(), barcodeImage.getHeight());
            }else {
                 bitMatrix = multiFormatWriter.encode(list.get(position).getBarCode(), BarcodeFormat.CODE_128, barcodeImage.getWidth(), barcodeImage.getHeight());
            }
            //translate bitMatrix  to bitMap
            Bitmap bitmap = Bitmap.createBitmap(barcodeImage.getWidth(), barcodeImage.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i < barcodeImage.getWidth(); i++) {
                for (int j = 0; j < barcodeImage.getHeight(); j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            //set the bitMap to imageView
            barcodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //if you want to show the barcode in dialog
        AlertDialog.Builder ImageDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        TextView title = new TextView(getContext());
        title.setText(list.get(position).getCardName() +"\n"+"("+list.get(position).getBarCode()+")");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setTextSize(20);
        ImageDialog.setCustomTitle(title);
        if (barcodeImage.getParent() != null) {
            ((ViewGroup) barcodeImage.getParent()).removeView(barcodeImage); // <- fix
        }

        ImageDialog.setView(barcodeImage);
        ImageDialog.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog mDialog = ImageDialog.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().getDecorView().setBackgroundResource(R.drawable.edittext_search_shape);
        mDialog.show();
    }

    private void showBarCodeDialog(String name, String barCode,String typeOfCode) {
        //generate barcode Image from string barcode
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix;
        try {
            if (typeOfCode.equals("QR_CODE")){
                bitMatrix = multiFormatWriter.encode(barCode, BarcodeFormat.QR_CODE, barcodeImage.getWidth(), barcodeImage.getHeight());
            }else {
                bitMatrix = multiFormatWriter.encode(barCode, BarcodeFormat.CODE_128, barcodeImage.getWidth(), barcodeImage.getHeight());
            }
            //translate bitMatrix  to bitMap
            Bitmap bitmap = Bitmap.createBitmap(barcodeImage.getWidth(), barcodeImage.getHeight(), Bitmap.Config.RGB_565);
            for (int i = 0; i < barcodeImage.getWidth(); i++) {
                for (int j = 0; j < barcodeImage.getHeight(); j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            //set the bitMap to imageView
            barcodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder ImageDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        TextView title = new TextView(getContext());
        title.setText(name +"\n"+"("+barCode+")");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setTextSize(20);
        ImageDialog.setCustomTitle(title);
        if (barcodeImage.getParent() != null) {
            ((ViewGroup) barcodeImage.getParent()).removeView(barcodeImage); // <- fix
        }
        ImageDialog.setView(barcodeImage);
        ImageDialog.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
                onPause();
            }
        });
        AlertDialog mDialog = ImageDialog.create();
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().getDecorView().setBackgroundResource(R.drawable.edittext_search_shape);
        mDialog.show();
    }

    public static double arcDistance(double lat1, double lon1, double lat2, double lon2) {
        //return SloppyMath.haversinMeters(lat1, lon1, lat2, lon2);
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = 0.0;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onLocationChanged(Location location) {
        Card cardZaDialog = null;
        System.out.println(location.getLatitude()+"==="+location.getLongitude());
        if(scannedLocations.size()>0) {
            for (GeoLocation g : scannedLocations) {
                double sloppyMathDistance = arcDistance(g.getLatitude(), g.getLongitude(), location.getLatitude(), location.getLongitude());
                System.out.println("SloppyMath Distance between my location and location on phone " + sloppyMathDistance);
                g.setDistance(sloppyMathDistance); //set the distance here
                if (sloppyMathDistance <= 60 && popUpFlag == false) {
                    cardZaDialog = list.stream().filter(card -> card.getCardName().equals(g.getCard_Id())).findFirst().get();
                    System.out.println("=====" + cardZaDialog.getCardName());
                    onPause();
                    showBarCodeDialog(g.getCard_Id(), cardZaDialog.getBarCode(), cardZaDialog.getTypeOfCode());
                    popUpFlag = true;
                }
            }

            //let's sort them, first create a comparator in GeoLocation class and with stream sort them, and pop up the first one
            List<GeoLocation> orderByDistanceList = scannedLocations.stream().sorted(GeoLocation.comapreByDistance).collect(Collectors.toList());
            sortCards(orderByDistanceList);
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortCards(List<GeoLocation> orderByDistanceList) {
        //this function only sort the cards
        List<String> cardsNames = orderByDistanceList.stream().map(GeoLocation::getCard_Id).distinct().collect(Collectors.toList());
        for(String name:cardsNames){
            Card sortCard = list.stream().filter(card -> card.getCardName().equals(name)).findFirst().get();
            sortedCards.add(sortCard);
        }

        customAdapter = new CustomAdapter(getContext(), sortedCards, this);
        list=sortedCards;
        scannedCards.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
        onPause();
    }
    @Override
    public void onPause() {
        locationManager.removeUpdates(this);
        super.onPause();
    }

}