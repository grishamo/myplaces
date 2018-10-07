package com.myplaces.myplaces;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
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
    private Marker placeMarker;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    private final int DEFAULT_ZOOM = 15;
    private MyPlace myPlace;

    private TextView mCurrentLocationTextView;
    private TextView mCurrentLocationCountryCity;
    private AppManager mAppMananger = AppManager.getInstance();
    private SpinnerData mSpinnerData;
    private ImageView mRedHeartIv;

    private ImageButton mCurrentLocationBtn;

    Button mSavePlaceBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_current_place, container, false);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mCurrentLocationTextView = rootView.findViewById(R.id.CurrentLocationAddress);
        mCurrentLocationCountryCity = rootView.findViewById(R.id.CurrentLocationCountryCity);
        mCurrentLocationBtn = rootView.findViewById(R.id.currentLocationBtn);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initSearchPlaceBtn();
        initSavePlaceBtn();
        initCurrentLocationBtn();

        return rootView;
    }

    private void initCurrentLocationBtn() {
        mCurrentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLastKnownLocation != null) {
                    LatLng locationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    BuildCustomPlace(locationLatLng);
                    SetCurrentLocationText(locationLatLng);
                    ChangeMarkerPosition(locationLatLng);
                    ChangeCameraPosition(locationLatLng);
                }
            }
        });
    }

    private void initSavePlaceBtn() {
        mSavePlaceBtn = rootView.findViewById(R.id.save_place_popup_btn);
        mSavePlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSavePlaceDialog();
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
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
        } catch (SecurityException e) {
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
                        LatLng locationLatLng;
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            locationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        } else {
                            Log.d(mTitle, "Current location is null. Using defaults.");
                            Log.e(mTitle, "Exception: %s", task.getException());

                            locationLatLng = mDefaultLocation;
                        }

                        BuildCustomPlace(locationLatLng);
                        SetCurrentLocationText(locationLatLng);
                        SetMarkerPosition(locationLatLng);
                        ChangeCameraPosition(locationLatLng);
                    }
                });
            }
        } catch (SecurityException e) {
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
            Address address = Utils.GetPlaceAddressByLatLng(getActivity(), latLng);
            String fullAddress = address.getAddressLine(0);
            String city = address.getLocality();
            String country = address.getCountryName();

            mCurrentLocationTextView.setText(fullAddress.split(",")[0]);
            mCurrentLocationCountryCity.setText(city + ", " + country);
        }
    }

    public void ShowSavePlaceDialog() {

        if(mLastKnownLocation == null) {
            getDeviceLocation();
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View dialogView = getLayoutInflater().inflate(R.layout.save_place_dialog, null);
        TextView locationTv = dialogView.findViewById(R.id.location_tv);
        TextView placeTitleTv = dialogView.findViewById(R.id.place_title_tv);
        ImageView placeImageIv = dialogView.findViewById(R.id.place_google_iv);
        ImageButton editBtn = dialogView.findViewById(R.id.edit_description_btn);
        ImageButton shareBtn = dialogView.findViewById(R.id.share_btn);
        Button saveBtn = dialogView.findViewById(R.id.save_btn);

        locationTv.setText(myPlace.getCity());
        placeTitleTv.setText(myPlace.getTitle());

        if (myPlace.getDefaultPhoto() != null) {
            placeImageIv.setImageBitmap(myPlace.getDefaultPhoto());
        }

        Spinner spinner = dialogView.findViewById(R.id.choosen_category_spinner);
        mSpinnerData = new SpinnerData();
        mSpinnerData.populate(getContext(), spinner);

        final AlertDialog alertDialog = builder.setView(dialogView).show();

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?daddr=" + myPlace.getAddress().getLatitude() + "," + myPlace.getAddress().getLongitude();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share in..."));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(AppManager.getInstance().isPlaceExist(myPlace))) {

                    String selectedCategory = mSpinnerData.getSelectedItemStr();
                    if (selectedCategory != null) {
                        myPlace.setCategory(selectedCategory);
                        Log.i("ShowSavePlaceDialog", selectedCategory);
                    }

                    AppManager.SetMenuItems(myPlace);
                    AppManager.getInstance().getMyPlaces().add(myPlace);
                    AppManager.getInstance().Save(getActivity());

                    ShowSaveAnimation();
                    //Todo: build DataStorage manager, that except any object and save it to defined storage
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.place_exist_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CurrentPlaceFragmet", "editBtn.setOnClickListener");
                OpenEditPlace();
            }
        });
    }

    public void OpenEditPlace() {
        String selectedCategory = mSpinnerData.getSelectedItemStr();
        if (selectedCategory != null) {
            myPlace.setCategory(selectedCategory);
        }

        Intent intent = new Intent(getActivity(), PlaceEditActivity.class);
        intent.putExtra("myplace", myPlace);
        intent.putExtra("action", "create");
        startActivity(intent);
    }

    public void ChangeMarkerPosition(LatLng position) {
        placeMarker.setPosition(position);
        ChangeCameraPosition(position);
    }

    public void SetMarkerPosition(LatLng position) {
        placeMarker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
    }

    private void ChangeCameraPosition(LatLng position) {
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
        mMap.setIndoorEnabled(false);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                BuildCustomPlace(latLng);
                SetCurrentLocationText(latLng);
                ChangeMarkerPosition(latLng);
                ChangeCameraPosition(latLng);
            }
        });

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
    public void FragmentSelect() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(rootView.getContext(), data);
                SetCurrentLocationText(place.getLatLng());
                ChangeMarkerPosition(place.getLatLng());
                Log.i(mTitle, "Place: " + place.getAddress());
                BuildGooglePlace(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(rootView.getContext(), data);
                // TODO: Handle the error.
                Log.i(mTitle, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void BuildGooglePlace(Place place) {
        myPlace = new MyPlace(place, rootView.getContext());
    }

    private void BuildCustomPlace(LatLng locationLatLng) {
        Address address = Utils.GetPlaceAddressByLatLng(getActivity(), locationLatLng);
        myPlace = new MyPlace(address);
    }

    public void ShowSaveAnimation(){
        mRedHeartIv = getActivity().findViewById(R.id.red_heard_iv);

        mRedHeartIv.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.move_heart_anim);
        mRedHeartIv.startAnimation(animation);
        mRedHeartIv.setVisibility(View.INVISIBLE);
    }

}
