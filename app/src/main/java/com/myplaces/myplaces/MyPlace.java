package com.myplaces.myplaces;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MyPlace implements Serializable
{
    private transient Address address;
    private byte[] mAddressByteArray; // for serialization

    private transient Bitmap defaultPhoto;
    private byte[] mImageByteArray; // for serialization

    private transient ArrayList<Bitmap> additionalPhotos;
    private List<byte[]> mAdditionalPhotosBytesArray;

    private transient GeoDataClient mGeoDataClient;

    private String city;
    private String country;

    private String streetAddress;

    private String category;
    private String phoneNumber;
    private String description;
    private String webURL;

    private String googlePlaceId;
    private String title;


    public MyPlace(Address address)
    {
        this.address = address;

        this.city = address.getLocality();
        this.country = address.getCountryName();
        this.streetAddress = address.getAddressLine(0);

        this.title = address.getAddressLine(0);
        this.phoneNumber = address.getPhone();
        this.webURL = address.getUrl();

        this.additionalPhotos = new ArrayList<>();
        this.mAdditionalPhotosBytesArray = new ArrayList<>();

    }

    public MyPlace(Place googlePlaceObj, Context ctx)
    {

        try
        {
            this.address = Utils.GetPlaceAddressByLatLng(ctx, googlePlaceObj.getLatLng());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mGeoDataClient = Places.getGeoDataClient(ctx, null);

        this.title = googlePlaceObj.getName().toString();

        this.city = this.address.getLocality();
        this.country = this.address.getCountryName();
        this.streetAddress = this.address.getAddressLine(0);
        this.googlePlaceId = googlePlaceObj.getId();
        this.phoneNumber = googlePlaceObj.getPhoneNumber() != null ? googlePlaceObj.getPhoneNumber().toString() : null;
        this.webURL = googlePlaceObj.getWebsiteUri() != null ? googlePlaceObj.getWebsiteUri().toString() : null;

        this.additionalPhotos = new ArrayList<>();
        this.mAdditionalPhotosBytesArray = new ArrayList<>();

        setGooglePlacePhoto(this.googlePlaceId);
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
    }

    public void setDefaultPhoto(Bitmap defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public ArrayList<Bitmap> getAdditionalPhotos() {
        return additionalPhotos;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    private void setGooglePlacePhoto(String googleId) {
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(googleId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {

                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();

                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                if (photoMetadataBuffer != null && photoMetadataBuffer.getCount() > 0) {
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);


                    // Get the attribution text.
                    CharSequence attribution = photoMetadata.getAttributions();
                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);


                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            if (bitmap != null) {
                                defaultPhoto = bitmap;
                            }
                        }
                    });


                }
                assert photoMetadataBuffer != null;
                photoMetadataBuffer.release();
            }

        });
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
