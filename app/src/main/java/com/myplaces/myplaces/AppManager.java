package com.myplaces.myplaces;

public class AppManager
{
    private static volatile AppManager instance;
    private static Object mutex = new Object();

    private AppManager()
    {

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
}
