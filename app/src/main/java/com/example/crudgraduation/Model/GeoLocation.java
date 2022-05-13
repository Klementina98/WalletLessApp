package com.example.crudgraduation.Model;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;
import java.util.Comparator;


@RequiresApi(api = Build.VERSION_CODES.N)
public class GeoLocation implements Parcelable {
    private double latitude;
    private double longitude;
    private String card_Id;
    private double distance;

    //let's make comparator
    public static Comparator<GeoLocation> comapreByDistance = Comparator.comparing(GeoLocation::getDistance);
    public GeoLocation() {
    }

    public GeoLocation(String card_Id, double latitude, double longitude) {
        this.card_Id = card_Id;
        this.latitude = latitude;
        this.longitude = longitude;

    }
    protected GeoLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        card_Id = in.readString();
    }

    public static final Creator<GeoLocation> CREATOR = new Creator<GeoLocation>() {
        @Override
        public GeoLocation createFromParcel(Parcel in) {
            return new GeoLocation(in);
        }

        @Override
        public GeoLocation[] newArray(int size) {
            return new GeoLocation[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(card_Id);
    }

    public String getCard_Id() {
        return card_Id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", card_Id='" + card_Id + '\'' +
                ", distance=" + distance +
                '}';
    }
}
