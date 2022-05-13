package com.example.crudgraduation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.crudgraduation.Model.GeoLocation;
import java.util.ArrayList;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Cards.db";
    private static final int DATABASE_VERSION = 1;

    //create scanned card table
    private static final String TABLE_NAME = "scanned_cards";
    private static final String COLUMN_ID = "_id"; //automaticlly incremented
    private static final String COLUMN_NAME = "card_name";
    private static final String COLUMN_BARCODE = "card_barcode";
    private static final String COLUMN_IMAGE = "card_image";
    private static final String COLUMN_TYPE = "card_type";

    //create locations table
    private static final String TABLE_NAME2 = "locations";
    private static final String COLUMN_LOCATION_ID = "_id"; //automaticlly incremented
    private static final String COLUMN_CARD_NAME = COLUMN_NAME; //id-to na card, foreign key
    private static final String COLUMN_LATITUDE = "card_latitude";
    private static final String COLUMN_LONGITUDE = "card_longitude";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //let's create query
         String query = "CREATE TABLE "+ TABLE_NAME +
                       " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                         COLUMN_NAME + " TEXT, " +
                         COLUMN_BARCODE + " TEXT, " +
                         COLUMN_IMAGE + " BLOB, "+
                         COLUMN_TYPE + " TEXT);";
         db.execSQL(query);

        //let's create query
        String query2 = "CREATE TABLE "+ TABLE_NAME2 +
                " (" + COLUMN_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                COLUMN_CARD_NAME + " TEXT, " +
                COLUMN_LATITUDE + " DOUBLE, " +
                COLUMN_LONGITUDE + " DOUBLE);";
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertCard(String name, String barcode, byte[] image, ArrayList<GeoLocation> locations,String typeOfCode){
        SQLiteDatabase db = this.getWritableDatabase(); //so we can write to our table
        ContentValues cv = new ContentValues();  //in this cv we are gonna store all data from the app

        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_BARCODE,barcode);
        cv.put(COLUMN_IMAGE,image);
        cv.put(COLUMN_TYPE,typeOfCode);

        insertLocation(name,locations);

        //now use the SQLiteDatabase object to insert the data into the table
        long result = db.insert(TABLE_NAME,null,cv);
        if (result == -1){
            //Toast.makeText(context,"FAILED", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context,"Card Insert successfully!", Toast.LENGTH_SHORT).show();
        }

    }
    //insert Locations into table
    @RequiresApi(api = Build.VERSION_CODES.N)
    void insertLocation(String name, ArrayList<GeoLocation> geoLocations){
        SQLiteDatabase db = this.getWritableDatabase(); //so we can write to our table
        ContentValues cv = new ContentValues();  //in this cv we are gonna store all data from the app
        long result;
        for (GeoLocation loc:geoLocations){
            cv.put(COLUMN_CARD_NAME,name); //for each cardId place 2 locations
            cv.put(COLUMN_LATITUDE,loc.getLatitude());
            cv.put(COLUMN_LONGITUDE,loc.getLongitude());
            result = db.insert(TABLE_NAME2,null,cv);
        }
    }

    //display data to recycler view
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase(); // sqllitedatabase object so we can read data from our table
        Cursor cursor = null;
        if (db!=null){ //that means that we have some data in our db table
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readAllDataFromLocations(){
        String query = "SELECT * FROM " + TABLE_NAME2;
        SQLiteDatabase db = this.getReadableDatabase(); // sqllitedatabase object so we can read data from our table
        Cursor cursor = null;
        if (db!=null){ //that means that we have some data in our db table
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
    public boolean existCard(String cardName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where "+ COLUMN_NAME +"= '" +cardName + "'" , null);
        if (c.getCount()>0){
            return true;
        }else{
            return false;
        }
    }


    public void deleteCard(String cardId){
        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(cardId)});
        if (result == -1){
            //Toast.makeText(context,"Failed to delete", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context,"Card successfully deleted", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //delete Geolocation
    public void deleteGeoLocation(String cardName){
        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME2, COLUMN_CARD_NAME+"=?", new String[]{String.valueOf(cardName)});
        if (result == -1){
            //Toast.makeText(context,"Failed to delete", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(context,"Successfully deleted", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
