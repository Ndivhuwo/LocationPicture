package com.smartalgorithms.locationpictures;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.smartalgorithms.locationpictures.Dagger.AppComponent;
import com.smartalgorithms.locationpictures.Dagger.DaggerAppComponent;
import com.smartalgorithms.locationpictures.Services.LocationService;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class App extends Application implements HasActivityInjector {
    private static final String TAG = App.class.getSimpleName();

    public static boolean ENABLE_LOGS = true;
    private static Context context;
    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    private static AppComponent daggerAppComponent;
    private static Intent location_intent;

    @Override
    public void onCreate() {
        super.onCreate();

        daggerAppComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        daggerAppComponent.inject(this);

        context = getApplicationContext();
        location_intent = new Intent(this, LocationService.class);
    }

    public static Intent getLocationIntent() {
        return location_intent;
    }

    public static Context getAppContext() {
        if (context == null)
            context = getAppContext();
        return context;
    }

    public static AppComponent getDaggerAppComponent(){
        return daggerAppComponent;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
