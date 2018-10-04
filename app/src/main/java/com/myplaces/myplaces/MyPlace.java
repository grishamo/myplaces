package com.myplaces.myplaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyPlace implements Serializable
{
    private Address address;

    // should be..


    private String city;
    private String country;



    private String streetAddress;
    //

    private String category;
    private String phoneNumber;
    private Bitmap defaultPhoto;
    private String description;
    private ArrayList<Bitmap> additionalPhotos;
    private String webURL;



    private String googlePlaceId;
    private String title;
    // prive DateTime date;


    public MyPlace(Address address)
    {
        this.address = address;

        //
        this.city = address.getLocality();
        this.country = address.getCountryName();
        this.streetAddress = address.getAddressLine(0);
        //

        this.title = address.getAddressLine(0);
        this.phoneNumber = address.getPhone();
        this.webURL = address.getUrl();
    }

    public MyPlace(Place googlePlaceObj, Context ctx)
    {

        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        List<Address>addresses = null;
        this.title = googlePlaceObj.getName().toString();

        try
        {
            addresses = geocoder.getFromLocation(googlePlaceObj.getLatLng().latitude, googlePlaceObj.getLatLng().longitude, 1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // the rest

        this.city = addresses.get(0).getLocality();
        this.country = addresses.get(0).getCountryName();
        this.streetAddress = addresses.get(0).getAddressLine(0);
        this.address = addresses.get(0);
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

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Bitmap getDefaultPhoto() {
        return defaultPhoto;
    }

    public void setDefaultPhoto(Bitmap defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public ArrayList<Bitmap> getAdditionalPhotos() {
        return additionalPhotos;
    }

    public void setAdditionalPhotos(ArrayList<Bitmap> additionalPhotos) {
        this.additionalPhotos = additionalPhotos;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public ArrayList<Bitmap> GetAllImages()
    {
        ArrayList<Bitmap> allImages = getAdditionalPhotos();
        allImages.add(0,defaultPhoto);
        return allImages;
    }

}
