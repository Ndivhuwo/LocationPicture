package com.smartalgorithms.locationpictures.Home;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.App;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.Network.LocationNetAPI;
import com.smartalgorithms.locationpictures.Network.NearByPlacesNetAPI;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class HomeUseCase extends LiveData<String>{
    private static final String TAG = HomeUseCase.class.getSimpleName();
    private static final int SEARCH_RADIUS = 7000;

    private HomeContract.PresenterListener presenterListener;
    private Provider<Scheduler> subscribeSchedulerProvider;
    private Scheduler observeScheduler;
    private LocationNetAPI locationNetAPI;
    private LocationResponse locationResponse;
    private NearByPlacesNetAPI nearByPlacesNetAPI;
    private LoggingHelper loggingHelper;
    private LatLng coordinates;
    private boolean search = true;
    private Provider<LocalBroadcastManager> localBroadcastManagerProvider;
    private IntentFilter intentFilter;
    private CompositeDisposable compositeDisposable;
    private Provider<CompositeDisposable> compositeDisposableProvider;

    @Inject
    public HomeUseCase(HomeContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider,
                       Scheduler observeScheduler, LocationNetAPI locationNetAPI, LocationResponse locationResponse,
                       NearByPlacesNetAPI nearByPlacesNetAPI, LoggingHelper loggingHelper,
                       Provider<LocalBroadcastManager> localBroadcastManagerProvider, IntentFilter intentFilter,
                       Provider<CompositeDisposable> compositeDisposableProvider) {
        this.presenterListener = presenterListener;
        this.subscribeSchedulerProvider = subscribeSchedulerProvider;
        this.observeScheduler = observeScheduler;
        this.locationNetAPI = locationNetAPI;
        this.locationResponse = locationResponse;
        this.nearByPlacesNetAPI = nearByPlacesNetAPI;
        this.loggingHelper = loggingHelper;
        this.localBroadcastManagerProvider = localBroadcastManagerProvider;
        this.intentFilter = intentFilter;
        this.compositeDisposableProvider = compositeDisposableProvider;
    }

    public void resume() {
        presenterListener.requestPhonePermissions();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposableProvider != null) {
            if (compositeDisposable == null || compositeDisposable.isDisposed())
                compositeDisposable = compositeDisposableProvider.get();
        }
        return compositeDisposable;
    }

    public void getReverseGeocode(LatLng coordinates) {
        getCompositeDisposable().add(locationNetAPI.getAddressFromCoordinatesSingle(coordinates)
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing getReverseGeocode Single"))
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .subscribe(addressResponse -> {
                    setValue(addressResponse.getResults().get(0).getFormatted_address());
                    presenterListener.onGetAddress(addressResponse);
                        },
                        error -> {
                            locationResponse.setSuccess(false);
                            locationResponse.setMessage("Error: " + error.getMessage());
                            presenterListener.onGetAddress(locationResponse);
                        }));
    }

    @Override
    protected void onActive() {
        loggingHelper.i(TAG, "onActive");
        localBroadcastManagerProvider.get().registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onInactive() {
        loggingHelper.i(TAG, "onInactive");
        localBroadcastManagerProvider.get().unregisterReceiver(receiver);
        if(compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public void getNearByPlaces(LatLng coordinates, int searchRadius) {
        getCompositeDisposable().add(nearByPlacesNetAPI.getNearByPlacesSingle(coordinates, searchRadius)
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing getNearByPlaces Observable"))
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .subscribe(nearByPlacesResponse1 -> presenterListener.onGetNearByPlaces(nearByPlacesResponse1, null),
                        error -> presenterListener.onGetNearByPlaces(null, error.getMessage())));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (coordinates == null && search) {
                coordinates = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
                loggingHelper.i("Broadcast receiver", "lat: " + coordinates.latitude + " lang: " + coordinates.longitude);
                search = false;
                presenterListener.requestAddress(coordinates);
                presenterListener.requestNearByPlaces(coordinates, SEARCH_RADIUS);
            } else if (coordinates.longitude != bundle.getDouble("lng") && coordinates.latitude != bundle.getDouble("lat") && search) {
                coordinates = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
                loggingHelper.i("Broadcast receiver", "lat: " + coordinates.latitude + " lang: " + coordinates.longitude);
                search = false;
                presenterListener.requestAddress(coordinates);
                presenterListener.requestNearByPlaces(coordinates, SEARCH_RADIUS);
            }

        }
    };

    public void refreshLocationListener() {
        presenterListener.displayReloadLottieAnimation(true);
        getCompositeDisposable().add(Single.timer(500, TimeUnit.MILLISECONDS)
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing timer Single"))
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .subscribe(time -> {
                    search = true;
                    coordinates = null;
                    presenterListener.requestLocation();
                    presenterListener.displayReloadLottieAnimation(false);
                }));

    }
}
