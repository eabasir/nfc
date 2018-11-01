package ansari.com.nfcaesdemo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;



public class App extends android.app.Application {


    private static App instance;

    public static String PACKAGE_NAME;
    public static Activity CurrentActivity;
    public static Context context;
    public static PackageManager packageManager;
    public static final Handler HANDLER = new Handler();




    public static App getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    public App ()
    {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            context = getApplicationContext();

            try {
                PACKAGE_NAME = context.getPackageName();
                packageManager = getPackageManager();

            } catch (Exception e) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}