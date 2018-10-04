package com.myplaces.myplaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.android.gms.location.places.Place;

import java.io.IOException;
import java.io.Serializable;

public class MyPlace implements Serializable
{
    private String title;
    private String description;
    private String location;
    private String category;
    private String phoneNumber;
    private Bitmap photo;
    private String webURL;
    //private Place googlePlace;

    public MyPlace(String title, String description, String location, Bitmap photo)
    {
        this.title = title;
        this.description = description;
        this.location = location;
        this.photo = photo;
    }

    public MyPlace(Place googlePlaceObj) {
        this.title = googlePlaceObj.getName().toString();
        this.location = googlePlaceObj.getAddress().toString();
        this.phoneNumber = googlePlaceObj.getPhoneNumber().toString();
        // this.googlePlace = googlePlaceObj;
        // Got an GetPath methods..
       // this.webURL = googlePlaceObj.getWebsiteUri().getPath();
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    private void writeObject(java.io.ObjectOutputStream out) throws IOException
//    {
//        photo.compress(Bitmap.CompressFormat.JPEG,50, out);
//        out.defaultWriteObject();
//    }
//
//    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
//    {
//        photo = BitmapFactory.decodeStream(in);
//        in.defaultReadObject();
//    }

}
