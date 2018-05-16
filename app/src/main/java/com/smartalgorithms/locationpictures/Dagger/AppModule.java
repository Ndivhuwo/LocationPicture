package com.smartalgorithms.locationpictures.Dagger;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartalgorithms.locationpictures.App;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.Models.VenueResponse;
import com.smartalgorithms.locationpictures.Network.LocationNetAPI;
import com.smartalgorithms.locationpictures.Network.NearByPlacesNetAPI;
import com.smartalgorithms.locationpictures.Network.NetworkUseCase;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */
@Module
public class AppModule {
    @Provides
    @Annotations.ApplicationContext
    public Context provideContext(App application){
        return application.getApplicationContext();
    }

    @Provides
    public Intent providesIntent(){
        return new Intent();
    }
    
    @Provides
    public Bundle provideBundle() {
        return new Bundle();
    }

    @Singleton
    @Provides
    public ResourcesHelper provideResourcesHelper(@Annotations.ApplicationContext Context context) {
        return new ResourcesHelper(context);
    }

    @Provides
    public GeneralHelper provideGeneralHelper(@Annotations.ApplicationContext Context context, Gson gson) {
        return new GeneralHelper(context, gson);
    }

    @Singleton
    @Provides
    public LoggingHelper provideLoggingHelper() {
        return new LoggingHelper();
    }

    @Singleton
    @Provides
    public IntentFilter provideIntentFilter() {
        return new IntentFilter("com.smartalgorithms.locationpictures");
    }

    @Singleton
    @Provides
    public Toast provideToast(@Annotations.ApplicationContext Context context) {
        return new Toast(context);
    }

    @Singleton
    @Provides
    public NetworkUseCase provideNetworkUseCase(LoggingHelper loggingHelper) {
        return new NetworkUseCase(loggingHelper);
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Integer.class, new GeneralHelper.IntegerTypeAdapter())
                .serializeNulls()
                .create();
    }

    @Singleton
    @Provides
    public LocationNetAPI provideLocationNetAPI(NetworkUseCase networkUseCase, LoggingHelper loggingHelper, ResourcesHelper resourcesHelper, LocationResponse locationResponse, GeneralHelper generalHelper) {
        return new LocationNetAPI(networkUseCase, loggingHelper, resourcesHelper, locationResponse, generalHelper);
    }

    @Singleton
    @Provides
    public LocationResponse provideLocationResponse(GeneralHelper generalHelper) {
        return new LocationResponse(generalHelper);
    }

    @Singleton
    @Provides
    public NearByPlacesNetAPI provideNearByPlacesNetAPI(NetworkUseCase networkUseCase, LoggingHelper loggingHelper, ResourcesHelper resourcesHelper, NearByPlacesResponse nearByPlacesResponse, GeneralHelper generalHelper, VenueResponse venueResponse) {
        return new NearByPlacesNetAPI(networkUseCase, loggingHelper, resourcesHelper, generalHelper, nearByPlacesResponse, venueResponse);
    }

    @Singleton
    @Provides
    public NearByPlacesResponse provideNearByPlacesResponse(GeneralHelper generalHelper) {
        return new NearByPlacesResponse(generalHelper);
    }

    @Singleton
    @Provides
    public VenueResponse provideVenueResponse() {
        return new VenueResponse();
    }

    @Provides
    static Scheduler provideBackgroundScheduler() {
        return Schedulers.newThread();
    }
}
