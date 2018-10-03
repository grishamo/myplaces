package com.myplaces.myplaces;

import android.net.Uri;

import com.google.android.gms.location.places.Place;

import java.net.URL;

public class MyPlace
{
    private String title;
    private String description;
    private String location;
    private String category;
    private String phoneNumber;
    private Uri webUri;
    private Place googlePlace;

    public MyPlace(String title, String description, String location)
    {
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public MyPlace(Place googlePlaceObj)
    {
        this.title = googlePlaceObj.getName().toString();
        this.location = googlePlaceObj.getAddress().toString();
        this.phoneNumber = googlePlaceObj.getPhoneNumber().toString();
        this.googlePlace = googlePlaceObj;
        // Got an GetPath methods..
        this.webUri = googlePlaceObj.getWebsiteUri();

    }

    public Uri getWebUri() {
        return webUri;
    }

    public void setWebUri(Uri webUri) {
        this.webUri = webUri;
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



}
