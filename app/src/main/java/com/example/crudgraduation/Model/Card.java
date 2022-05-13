package com.example.crudgraduation.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Card {

    private String cardId;
    private String cardName;
    private String cardImage;
    private String barCode;
    private byte [] localImage;
    private String typeOfCode;
    HashMap<String,GeoLocation> locations;
    ArrayList<GeoLocation> test;

    //need default constructor for firebase
    public Card() {
    }

    public Card(String cardId,String cardName, String barCode, byte [] localImage,String typeOfCode) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.barCode = barCode;
        this.localImage = localImage;
        this.typeOfCode = typeOfCode;
    }
    public Card(String cardId,String cardName, String barCode, byte [] localImage, HashMap<String,GeoLocation> locations) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.barCode = barCode;
        this.localImage = localImage;
        this.locations = locations;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public byte[] getLocalImage() {
        return localImage;
    }

    public void setLocalImage(byte[] localImage) {
        this.localImage = localImage;
    }


    public HashMap<String, GeoLocation> getLocations() {
        return locations;
    }

    public void setLocations(HashMap<String, GeoLocation> locations) {
        this.locations = locations;
        Collection<GeoLocation> values = locations.values();
        this.test = new ArrayList<GeoLocation>(values);
    }

    public ArrayList<GeoLocation> getTest() {
        return test;
    }

    public void setTest(ArrayList<GeoLocation> test) {
        this.test = test;
    }

    public String getTypeOfCode() {
        return typeOfCode;
    }

    public void setTypeOfCode(String typeOfCode) {
        this.typeOfCode = typeOfCode;
    }
}
