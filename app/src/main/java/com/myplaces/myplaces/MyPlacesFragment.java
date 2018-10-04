package com.myplaces.myplaces;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class MyPlacesFragment extends Fragment implements IPageFragment
{
    private GoogleMap mMap;
    private int mPageIcon = R.drawable.ic_location_on_black_24dp;
    private String mTitle = "My Places :)";
    private ListView placesListView;
    private ArrayList<MyPlace> myPlacesArrayList = new ArrayList<>();
    private View rootView;
    private Spinner cityDropDown;
    private Spinner categoryDropDown;
    private Spinner countryDropDown;

    public MyPlacesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.myplaces_fragment, container, false);

        placesListView = rootView.findViewById(R.id.myplaces_Listview);
        cityDropDown = rootView.findViewById(R.id.dropDown_city);
        countryDropDown = rootView.findViewById(R.id.dropDown_country);
        categoryDropDown = rootView.findViewById(R.id.dropDown_category);
        
        PopulatePlacesListView();

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent placeFullyInfoIntent = new Intent(getActivity(), PlaceFullyInfoActivity.class);
                startActivity(placeFullyInfoIntent);
            }
        });


//        myPlacesArrayList.add(new MyPlace("Lol","ROFL","STFU BITCH"));
//        myPlacesArrayList.add(new MyPlace("HAHAHAHA","SDSDAS","STFU SDASDA"));
//        myPlacesArrayList.add(new MyPlace("LCCCol","RORRRFL","STFU WWWW"));
//        myPlacesArrayList.add(new MyPlace("Lol","ROFL","STFU BITCH"));
//        myPlacesArrayList.add(new MyPlace("HAHAHAHA","SDSDAS","STFU SDASDA"));
//        myPlacesArrayList.add(new MyPlace("LCCCol","RORRRFL","STFU WWWW"));
//        myPlacesArrayList.add(new MyPlace("Lol","ROFL","STFU BITCH"));
//        myPlacesArrayList.add(new MyPlace("HAHAHAHA","SDSDAS","STFU SDASDA"));
//        myPlacesArrayList.add(new MyPlace("LCCCol","RORRRFL","STFU WWWW"));
//        myPlacesArrayList.add(new MyPlace("Lol","ROFL","STFU BITCH"));
//        myPlacesArrayList.add(new MyPlace("HAHAHAHA","SDSDAS","STFU SDASDA"));
//        myPlacesArrayList.add(new MyPlace("LCCCol","RORRRFL","STFU WWWW"));



        String[] categoryList = new String[]{"bar", "food", "travels"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoryList);
        categoryDropDown.setAdapter(categoryAdapter);


        String[] cityList = new String[]{"rehovot", "holon", "telaviv"};
        ArrayAdapter<String> cityaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityList);
        cityDropDown.setAdapter(cityaAdapter);


        String[] countryList = new String[]{"israel", "canada", "usa"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countryList);
        countryDropDown.setAdapter(countryAdapter);


        return rootView;
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
        PopulatePlacesListView();
    }

    public void PopulatePlacesListView()
    {
        myPlacesArrayList = AppManager.getInstance().getMyPlaces();
        MyPlacesCustomAdapter mpca = new MyPlacesCustomAdapter(myPlacesArrayList, getActivity());
        placesListView.setAdapter(mpca);
    }


}
