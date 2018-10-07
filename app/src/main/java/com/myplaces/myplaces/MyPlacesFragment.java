package com.myplaces.myplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
    private ImageButton sortBtn;

    private ImageButton addPlaceBtn;

    private TextView emptyListTextview;

    private PopupMenu categoryPopupMenu;
    private PopupMenu cityPopupMenu;
    private PopupMenu countryPopupMenu;

    private ArrayList<MyPlace> filteredPlaces = new ArrayList<>();


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
        sortBtn = rootView.findViewById(R.id.sort_btn);
        categoryPopupMenu = new PopupMenu(getActivity(), categoryBtn);
        countryPopupMenu = new PopupMenu(getActivity(), countryBtn);
        cityPopupMenu = new PopupMenu(getActivity(), cityBtn);

        PopulatePlacesListView();
        PopulateMenuContents();

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                TextView titleTv = view.findViewById(R.id.place_name);
                String titleStr = titleTv.getText().toString();
                MyPlace myPlace = AppManager.getInstance().getPlaceById(titleStr);

                Intent placeFullyInfoIntent = new Intent(getActivity(), PlaceFullyInfoActivity.class);
                placeFullyInfoIntent.putExtra("myplace", myPlace);
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

        categoryPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                categoryBtn.setText(menuItem.getTitle().toString());
                return false;
            }
        });

        cityPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                cityBtn.setText(menuItem.getTitle().toString());
                return false;
            }
        });

        countryPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                countryBtn.setText(menuItem.getTitle().toString());
                return false;
            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                filteredPlaces.clear();

                String allWord = getText(R.string.all_word).toString();
                String categoryWord = getText(R.string.option_category).toString();
                String cityWord = getText(R.string.option_city).toString();
                String countryWord = getText(R.string.option_country).toString();

                String chosenCategory = categoryBtn.getText().toString();
                String chosenCity = cityBtn.getText().toString();
                String chosenCountry = countryBtn.getText().toString();

                for(MyPlace place : AppManager.getInstance().getMyPlaces())
                {
                    if(chosenCategory.equals(allWord) || chosenCategory.equals(categoryWord) || chosenCategory.equals(place.getCategory()))
                    {
                        if(chosenCountry.equals(allWord) || chosenCountry.equals(countryWord) || chosenCountry.equals(place.getCountry()))
                        {
                            if(chosenCity.equals(allWord) || chosenCity.equals(cityWord) || chosenCity.equals(place.getCity()))
                            {
                                filteredPlaces.add(place);
                            }
                        }
                    }
                }
                PopulateFilteredPlaces();
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


        categoryBtn.setText(getText(R.string.option_category).toString());
        cityBtn.setText(getText(R.string.option_city).toString());
        countryBtn.setText(getText(R.string.option_country).toString());
    }

    public void PopulateFilteredPlaces()
    {
        MyPlacesCustomAdapter myPlaceCustomAdapter = new MyPlacesCustomAdapter(filteredPlaces, getActivity());
        placesListView.setAdapter(myPlaceCustomAdapter);
    }

}


