package com.myplaces.myplaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyPlace implements Serializable
{
    private transient Address address;
    private byte[] mAddressByteArray; // for serialization

    private transient Bitmap defaultPhoto;
    private byte[] mImageByteArray; // for serialization

    private transient ArrayList<Bitmap> additionalPhotos;

    private String city;
    private String country;

    private String streetAddress;

    private String category;
    private String phoneNumber;
    private String description;
    private String webURL;

    private String googlePlaceId;
    private String title;
    // prive DateTime date;


    public MyPlace(Address address)
    {
        this.address = address;

        this.city = address.getLocality();
        this.country = address.getCountryName();
        this.streetAddress = address.getAddressLine(0);

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
        this.googlePlaceId = googlePlaceObj.getId();
        this.additionalPhotos = new ArrayList<>();
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
        //return additionalPhotos != null ? additionalPhotos.get(0) : null;
    }

    public void setDefaultPhoto(Bitmap defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
        //additionalPhotos.add(defaultPhoto);
        //bitmapArraySize = additionalPhotos.size();
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
        //ArrayList<Bitmap> allImages = getAdditionalPhotos();
        //allImages.add(0,defaultPhoto);
        return getAdditionalPhotos();
    }

    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 111696345129311948L;
        public byte[] imageByteArray;
    }

    private void deSerialize() {
        if (mImageByteArray != null && mImageByteArray.length > 0) {
            setDefaultPhoto(BitmapFactory.decodeByteArray(mImageByteArray, 0, mImageByteArray.length));
            mImageByteArray = null;
        }

        if (mAddressByteArray != null && mAddressByteArray.length > 0) {
            setAddress(ParcelableUtil.unmarshall(mAddressByteArray, Address.CREATOR));
            mAddressByteArray = null;
        }
    }

    private void toSerializable() {
        if (getDefaultPhoto() != null) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                getDefaultPhoto().compress(Bitmap.CompressFormat.JPEG, 50, stream);
                mImageByteArray = stream.toByteArray();

                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (getAddress() != null) {
            mAddressByteArray = ParcelableUtil.marshall(getAddress());
        }

    }

    /** Included for serialization - write this layer to the output stream. */
    private void writeObject(ObjectOutputStream out) throws IOException {
        toSerializable();
        out.defaultWriteObject();
    }

    /** Included for serialization - read this object from the supplied input stream. */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject();
        deSerialize();
    }

}
