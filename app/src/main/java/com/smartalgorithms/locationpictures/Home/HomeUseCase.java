package com.smartalgorithms.locationpictures.Home;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.google.android.gms.maps.model.LatLng;
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

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class HomeUseCase {
    private static final String TAG = HomeUseCase.class.getSimpleName();
    private HomeContract.PresenterListener presenterListener;
    private Provider<Scheduler> subscribeSchedulerProvider;
    private Scheduler observeScheduler;
    private LocationNetAPI locationNetAPI;
    private LocationResponse locationResponse;
    private NearByPlacesNetAPI nearByPlacesNetAPI;
    private NearByPlacesResponse nearByPlacesResponse;
    private LifecycleOwner lifecycleOwner;
    private LoggingHelper loggingHelper;

    @Inject
    public HomeUseCase(LifecycleOwner lifecycleOwner, HomeContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider,
                       Scheduler observeScheduler, LocationNetAPI locationNetAPI, LocationResponse locationResponse,
                       NearByPlacesNetAPI nearByPlacesNetAPI, NearByPlacesResponse nearByPlacesResponse, LoggingHelper loggingHelper) {
        this.lifecycleOwner = lifecycleOwner;
        this.presenterListener = presenterListener;
        this.subscribeSchedulerProvider = subscribeSchedulerProvider;
        this.observeScheduler = observeScheduler;
        this.locationNetAPI = locationNetAPI;
        this.locationResponse = locationResponse;
        this.nearByPlacesNetAPI = nearByPlacesNetAPI;
        this.nearByPlacesResponse = nearByPlacesResponse;
        this.loggingHelper = loggingHelper;
    }

    public void resume() {
        presenterListener.requestPhonePermissions();
    }

    public void getReverseGeocode(LatLng coordinates) {
        locationNetAPI.getAddressFromCoordinatesSingle(coordinates)
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing getReverseGeocode Single"))
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY)))
                .subscribe(addressResponse -> presenterListener.onGetAddress(addressResponse),
                        error -> {
                            locationResponse.setSuccess(false);
                            locationResponse.setMessage("Error: " + error.getMessage());
                            presenterListener.onGetAddress(locationResponse);
                        });
    }

    public void getNearByPlaces(LatLng coordinates, int searchRadius) {
        nearByPlacesNetAPI.getNearByPlacesSingle(coordinates, searchRadius)
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing getNearByPlaces Observable"))
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY)))
                .subscribe(nearByPlacesResponse1 -> presenterListener.onGetNearByPlaces(nearByPlacesResponse1, null),
                        error -> presenterListener.onGetNearByPlaces(null, error.getMessage()));
    }
}
