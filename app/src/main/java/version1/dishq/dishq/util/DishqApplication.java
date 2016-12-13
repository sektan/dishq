package version1.dishq.dishq.util;


import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public final class DishqApplication extends android.support.multidex.MultiDexApplication{

    public static DishqApplication application;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(application);
    }
}
