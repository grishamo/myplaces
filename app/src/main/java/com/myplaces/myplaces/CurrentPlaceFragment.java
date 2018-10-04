package com.myplaces.myplaces;

import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentPlaceFragment extends Fragment implements OnMapReadyCallback, IPageFragment {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private View rootView;
    private Button SearchPlaceBtn;
    private int mPageIcon = R.drawable.ic_location_on_black_24dp;
    private String mTitle = "Current Location";
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Geocoder geocoder;
    private Location mLastKnownLocation;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    private final int REQUEST_PERMISSION_CAMERA = 2;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private final int DEFAULT_ZOOM = 15;

    private TextView mCurrentLocationTextView;
    private TextView mCurrentLocationCountryCity;
    Button mSavePlace_btn;
    RecyclerView recyclerView;
    List<MyPlaces> myPlacesList;
    PlaceAdapter placeAdapter;
    ImageButton takePic_btn;
    ImageView takenPicture_iv;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_current_place, container, false);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mCurrentLocationTextView = rootView.findViewById(R.id.CurrentLocationAddress);
        mCurrentLocationCountryCity = rootView.findViewById(R.id.CurrentLocationCountryCity);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initSearchPlaceBtn();
        initSavePlaceBtn();

        Log.i(mTitle, "onCreateView");

        return rootView;
    }

    private void initSavePlaceBtn() {
        mSavePlace_btn = rootView.findViewById(R.id.save_place_popup_btn);
        mSavePlace_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }

    private void getLocationPermission() {
            /*
             * Request location permission, so that we can get the location of the
             * device. The result of the permission request is handled by a callback,
             * onRequestPermissionsResult.
             */
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                //mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                //mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            LatLng lastLocationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            SetCurrentLocationText(lastLocationLatLng);
                            SetMarkerPosition(lastLocationLatLng);
                        } else {
                            Log.d(mTitle, "Current location is null. Using defaults.");
                            Log.e(mTitle, "Exception: %s", task.getException());

                            SetMarkerPosition(mDefaultLocation);
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void initSearchPlaceBtn() {
        SearchPlaceBtn = rootView.findViewById(R.id.SearchPlaceBtn);
        SearchPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(Objects.requireNonNull(getActivity()));
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    private void SetCurrentLocationText(LatLng latLng) {
        if (latLng != null) {
            try {
                List<Address>addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();

                mCurrentLocationTextView.setText(address.split(",")[0]);
                mCurrentLocationCountryCity.setText(city + ", " + country);
            } catch (IOException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    public void ShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView = getLayoutInflater().inflate(R.layout.save_place_dialog, null);
        TextView locationTv = dialogView.findViewById(R.id.location_tv);
        TextView placeTitleTv = dialogView.findViewById(R.id.place_title_tv);
        Button editBtn = dialogView.findViewById(R.id.edit_description_btn);
        Button shareBtn = dialogView.findViewById(R.id.share_btn);
        Button saveBtn = dialogView.findViewById(R.id.save_btn);

        recyclerView = dialogView.findViewById(R.id.categories_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myPlacesList = new ArrayList<>();
        myPlacesList.add(new MyPlaces("Bars", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Rest", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Hotel", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Brothel", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Custom", "bla", "bla"));
        myPlacesList.add(new MyPlaces("asd", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Cusagdsfhdtom", "bla", "bla"));
        myPlacesList.add(new MyPlaces("argfgbvhn", "bla", "bla"));
        myPlacesList.add(new MyPlaces("w45ergd", "bla", "bla"));
        myPlacesList.add(new MyPlaces("4w56tertesr", "bla", "bla"));

        placeAdapter = new PlaceAdapter(myPlacesList);
        recyclerView.setAdapter(placeAdapter);

        builder.setView(dialogView).show();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog();
            }
        });
    }

    public void ShowEditDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

       final View dialogView = getLayoutInflater().inflate(R.layout.custom_place_description_dialog, null);
        takePic_btn = dialogView.findViewById(R.id.add_image_ib);

        takePic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);

                takenPicture_iv = new ImageView(getContext());
                linearLayout = dialogView.findViewById(R.id.images_layout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);

                takenPicture_iv.setLayoutParams(params);
                linearLayout.addView(takenPicture_iv, 0);
            }
        });

        builder.setView(dialogView).show();
    }

    public void SetMarkerPosition(LatLng position) {

        mMap.addMarker(new MarkerOptions()
                .position(position)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)           // Sets the center of the map to Mountain View
                .zoom(DEFAULT_ZOOM)         // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    @Override
    public int GetPageIcon() {
        return mPageIcon;
    }

    @Override
    public String GetPageTitle() {
        return mTitle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                SetCurrentLocationText(place.getLatLng());
                SetMarkerPosition(place.getLatLng());
                Log.i(mTitle, "Place: " + place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(mTitle, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if(requestCode == REQUEST_PERMISSION_CAMERA){
            super.onActivityResult(requestCode, resultCode, data);

            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            takenPicture_iv.setImageBitmap(bitmap);
        }
    }
}
