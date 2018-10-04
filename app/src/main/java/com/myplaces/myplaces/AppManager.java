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


    // Constructor
    private AppManager()
    {
        myPlaces = new ArrayList<>();
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

}