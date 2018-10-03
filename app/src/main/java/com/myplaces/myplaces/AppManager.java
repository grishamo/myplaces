package com.myplaces.myplaces;

import java.util.ArrayList;

public class AppManager
{
    // Members
    private static volatile AppManager instance;
    private static Object mutex = new Object();
    private ArrayList<MyPlace> myPlaces;



    // Constructor
    private AppManager()
    {
        myPlaces = new ArrayList<>();
    }

    public static AppManager getInstance() {
        AppManager result = instance;
        if (result == null)
        {
            synchronized (mutex)
            {
                result = instance;
                if (result == null)
                    instance = result = new AppManager();
            }
        }
        return result;
    }


    // Setters Getters

    public ArrayList<MyPlace> getMyPlaces() {
        return myPlaces;
    }

    public void setMyPlaces(ArrayList<MyPlace> myPlaces)
    {
        this.myPlaces = myPlaces;
    }





}
