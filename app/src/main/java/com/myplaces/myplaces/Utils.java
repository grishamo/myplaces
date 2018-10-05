package com.myplaces.myplaces;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static Address GetPlaceAddressByLatLng(Context contxt, LatLng latLng) {
        Geocoder geocoder = new Geocoder(contxt, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
