package com.smartalgorithms.locationpictures.Home;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.Network.LocationNetAPI;
import com.smartalgorithms.locationpictures.Network.NearByPlacesNetAPI;

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

    @Inject
    public HomeUseCase(HomeContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider,
                       Scheduler observeScheduler, LocationNetAPI locationNetAPI, LocationResponse locationResponse,
                       NearByPlacesNetAPI nearByPlacesNetAPI, NearByPlacesResponse nearByPlacesResponse) {
        this.presenterListener = presenterListener;
        this.subscribeSchedulerProvider = subscribeSchedulerProvider;
        this.observeScheduler = observeScheduler;
        this.locationNetAPI = locationNetAPI;
        this.locationResponse = locationResponse;
        this.nearByPlacesNetAPI = nearByPlacesNetAPI;
        this.nearByPlacesResponse = nearByPlacesResponse;
    }

    public void resume() {
        presenterListener.requestPhonePermissions();
    }

    public void getReverseGeocode(LatLng coordinates) {
        locationNetAPI.getAddressFromCoordinatesSingle(coordinates)
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .subscribe(addressResponse -> presenterListener.onGetAddress(addressResponse),
                        error -> {
                            locationResponse.setSuccess(false);
                            locationResponse.setMessage("Error: " + error.getMessage());
                            presenterListener.onGetAddress(locationResponse);
                        });
    }

    public void getNearByPlaces(LatLng coordinates, int searchRadius) {
        nearByPlacesNetAPI.getNearByPlacesSingle(coordinates, searchRadius)
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .subscribe(nearByPlacesResponse1 -> presenterListener.onGetNearByPlaces(nearByPlacesResponse1, null),
                        error -> presenterListener.onGetNearByPlaces(null, error.getMessage()));
    }
}
