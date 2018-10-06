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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class MyPlacesFragment extends Fragment implements IPageFragment
{
    private GoogleMap mMap;
    private int mPageIcon = R.drawable.ic_star_black_24dp;
    private String mTitle = "My Places :)";
    private ListView placesListView;

    private View rootView;

    private Button cityBtn;
    private Button categoryBtn;
    private Button countryBtn;
    private ImageButton addPlaceBtn;

    private TextView emptyListTextview;

    private PopupMenu categoryPopupMenu;
    private PopupMenu cityPopupMenu;
    private PopupMenu countryPopupMenu;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.myplaces_fragment, container, false);

        placesListView = rootView.findViewById(R.id.myplaces_Listview);
        cityBtn = rootView.findViewById(R.id.city_btn);
        countryBtn = rootView.findViewById(R.id.country_btn);
        categoryBtn = rootView.findViewById(R.id.category_btn);
        emptyListTextview = rootView.findViewById(R.id.empty_list_textview);
        addPlaceBtn = rootView.findViewById(R.id.add_place_btn);

        categoryPopupMenu = new PopupMenu(getActivity(), categoryBtn);
        countryPopupMenu = new PopupMenu(getActivity(), countryBtn);
        cityPopupMenu = new PopupMenu(getActivity(), cityBtn);
        
        PopulatePlacesListView();
        PopulateMenuContents();

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent placeFullyInfoIntent = new Intent(getActivity(), PlaceFullyInfoActivity.class);
                startActivity(placeFullyInfoIntent);
            }
        });

        cityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityPopupMenu.show();
            }
        });

        countryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryPopupMenu.show();
            }
        });

        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPopupMenu.show();
            }
        });

        addPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlaceEditActivity.class);
                intent.putExtra("action", "create");
                startActivity(intent);
            }
        });
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
        PopulateMenuContents();
    }

    public void PopulatePlacesListView()
    {
        ArrayList<MyPlace> myPlacesArrayList = AppManager.getInstance().getMyPlaces();
        MyPlacesCustomAdapter myPlaceCustomAdapter = new MyPlacesCustomAdapter(myPlacesArrayList, getActivity());

        if(myPlacesArrayList.size() > 0) {
            emptyListTextview.setVisibility(View.INVISIBLE);
        }

        placesListView.setAdapter(myPlaceCustomAdapter);
    }

    public void PopulateMenuContents()
    {
        ArrayList<String> categories = AppManager.getInstance().getCategoriesList();
        ArrayList<String> countries = AppManager.getInstance().getCountriesList();
        ArrayList<String> cities = AppManager.getInstance().getCitiesList();

        if(categories.size() != categoryPopupMenu.getMenu().size())
        {
            categoryPopupMenu.getMenu().clear();
            for(String category: categories)
            {
                categoryPopupMenu.getMenu().add(category);
            }
        }

        if(cities.size() != cityPopupMenu.getMenu().size())
        {
            cityPopupMenu.getMenu().clear();
            for(String city: cities)
            {
                cityPopupMenu.getMenu().add(city);
            }
        }

        if(countries.size() != countryPopupMenu.getMenu().size())
        {
            countryPopupMenu.getMenu().clear();
            for(String country: countries)
            {
                countryPopupMenu.getMenu().add(country);
            }
        }

    }

}
