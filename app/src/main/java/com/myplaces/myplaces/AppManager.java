package com.myplaces.myplaces;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AppManager implements Serializable
{
    // Members
    private static volatile AppManager instance;
    private static transient Object mutex = new Object();
    private ArrayList<MyPlace> myPlaces;
    private ArrayList<String> categoriesList;
    private ArrayList<String> citiesList;
    private ArrayList<String> countriesList;

    // Constructor
    private AppManager()
    {

        myPlaces = new ArrayList<>();
        categoriesList = new ArrayList<>();
        citiesList = new ArrayList<>();
        countriesList = new ArrayList<>();
        categoriesList.add(App.getContext().getResources().getString(R.string.all_word));
        citiesList.add(App.getContext().getResources().getString(R.string.all_word));
        countriesList.add(App.getContext().getResources().getString(R.string.all_word));
    }

    public static AppManager getInstance()
    {
        if (instance == null)
        {
            synchronized (mutex)
            {
                if (instance == null)
                {
                    instance  = new AppManager();
                }
            }
        }
        return instance;
    }


    // Setters Getters

    public ArrayList<MyPlace> getMyPlaces() {
        return myPlaces;
    }

    public void setMyPlaces(ArrayList<MyPlace> myPlaces)
    {
        this.myPlaces = myPlaces;
    }

    public ArrayList<String> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(ArrayList<String> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public ArrayList<String> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(ArrayList<String> citiesList) {
        this.citiesList = citiesList;
    }

    public ArrayList<String> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(ArrayList<String> countriesList) {
        this.countriesList = countriesList;
    }



    // Storage shit..

    public void Save(Context ctx)
    {
        try
        {
            FileOutputStream fos = ctx.openFileOutput("placesFile", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AppManager.getInstance());
            oos.close();
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load(Context ctx)
    {
        if (fileExist(ctx, "placesFile"))
        {
            try
             {
                FileInputStream fis = ctx.openFileInput("placesFile");
                ObjectInputStream ois = new ObjectInputStream(fis);

                instance = (AppManager)ois.readObject();
             }

            catch (FileNotFoundException e)
            {
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

    }

    private static boolean fileExist(Context ctx, String fname)
    {
        File file = ctx.getApplicationContext().getFileStreamPath(fname);
        return file.exists();
    }




    // CHECK FOR DIVIDING VALIDATION AND INSERTION SEPERATELY!
    public static void SetMenuItems(MyPlace myPlace) {
        if (myPlace.getCategory() != null &&
                !AppManager.getInstance().getCategoriesList().contains(myPlace.getCategory())) {
            AppManager.getInstance().getCategoriesList().add(myPlace.getCategory());
        }

        if (!AppManager.getInstance().getCitiesList().contains(myPlace.getCity())) {
            AppManager.getInstance().getCitiesList().add(myPlace.getCity());
        }

        if (!AppManager.getInstance().getCountriesList().contains(myPlace.getCountry())) {
            AppManager.getInstance().getCountriesList().add(myPlace.getCountry());
        }
    }

    public boolean isPlaceExist(MyPlace mPlaceItem) {
        boolean returnVal = false;
        //returnVal = getMyPlaces().contains(mPlaceItem);

        for (MyPlace place : myPlaces) {
            if( mPlaceItem.getTitle().equals(place.getTitle()) ){
                returnVal = true;
                break;
            }
        }

        return returnVal;
    }

    public MyPlace getPlaceById(String titleStr) {

        for (MyPlace place : myPlaces) {
            if( titleStr.equals(place.getTitle())){
                return place;
            }
        }

        return  null;
    }

    public void UpdatePlace(String oldPlaceId, MyPlace mPlaceItem) {

        int oldItemIndex = -1;

        for (MyPlace place : myPlaces) {
            if( oldPlaceId.equals(place.getTitle())){
                oldItemIndex = myPlaces.indexOf(place);
                break;
            }
        }

        if(oldItemIndex != -1){
             myPlaces.set(oldItemIndex, mPlaceItem);
        }

    }

    public void RemovePlaceById(String placeId) {
        int index = -1;

        for (MyPlace place : myPlaces) {
            if( placeId.equals(place.getTitle())){
                index = myPlaces.indexOf(place);
                break;
            }
        }

        if(index != -1){
            myPlaces.remove(index);
        }
    }

    // TO BE CONTINUE !
//    public static void checkMyPlaceObjectDuplicateBeforeInsertion(MyPlace myPlace)
//    {
//        if(!AppManager.getInstance().getMyPlaces().contains(myPlace))
//    }






    //

}
