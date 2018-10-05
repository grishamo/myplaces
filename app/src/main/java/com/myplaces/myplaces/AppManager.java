package com.myplaces.myplaces;

import android.content.Context;
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
                Toast.makeText(ctx,"NOTHING TO LOAD IDIOT!", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(ctx,"NOTHING TO LOAD IDIOT!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                Toast.makeText(ctx,"NOTHING TO LOAD IDIOT!", Toast.LENGTH_SHORT).show();
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
    public static void SetMenuItemss(MyPlace myPlace)
    {
        if(!AppManager.getInstance().getCategoriesList().contains(myPlace.getCategory()))
        {
            AppManager.getInstance().getCategoriesList().add(myPlace.getCategory());
        }

        if(!AppManager.getInstance().getCitiesList().contains(myPlace.getCity()))
        {
            AppManager.getInstance().getCitiesList().add(myPlace.getCity());
        }

        if(!AppManager.getInstance().getCountriesList().contains(myPlace.getCountry()))
        {
            AppManager.getInstance().getCountriesList().add(myPlace.getCountry());
        }
    }

    // TO BE CONTINUE !
//    public static void checkMyPlaceObjectDuplicateBeforeInsertion(MyPlace myPlace)
//    {
//        if(!AppManager.getInstance().getMyPlaces().contains(myPlace))
//    }






    //

}
