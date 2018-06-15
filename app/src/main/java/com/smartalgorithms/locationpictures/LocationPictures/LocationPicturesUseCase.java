package com.smartalgorithms.locationpictures.LocationPictures;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.Network.NearByPlacesNetAPI;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class LocationPicturesUseCase {
    private static final String TAG = LocationPicturesUseCase.class.getSimpleName();
    private LocationPicturesContract.PresenterListener presenterListener;
    private Provider<Scheduler> subscribeSchedulerProvider;
    private Scheduler observeScheduler;
    private NearByPlacesNetAPI nearByPlacesNetAPI;
    private GeneralHelper generalHelper;
    private int currentDistance = 0;
    private LifecycleOwner lifecycleOwner;
    private LoggingHelper loggingHelper;

    @Inject
    LocationPicturesUseCase(LifecycleOwner lifecycleOwner, LocationPicturesContract.PresenterListener presenterListener,
                                   Provider<Scheduler> subscribeSchedulerProvider, Scheduler observeScheduler,
                                   NearByPlacesNetAPI nearByPlacesNetAPI, GeneralHelper generalHelper, LoggingHelper loggingHelper) {
        this.lifecycleOwner = lifecycleOwner;
        this.presenterListener = presenterListener;
        this.subscribeSchedulerProvider = subscribeSchedulerProvider;
        this.observeScheduler = observeScheduler;
        this.nearByPlacesNetAPI = nearByPlacesNetAPI;
        this.generalHelper = generalHelper;
        this.loggingHelper = loggingHelper;
    }

    void resume(String placeJSON) {
        requestImages(placeJSON);
    }

    private void requestImages(String placeJSON) {
        NearByPlacesResponse.Response.Group.Item.Venue venue = (NearByPlacesResponse.Response.Group.Item.Venue) generalHelper.getObjectFromJson(placeJSON, NearByPlacesResponse.Response.Group.Item.Venue.class);
        currentDistance = venue.getLocation().getDistance();
        nearByPlacesNetAPI.getVenueDetailsSingle(venue.getId())
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing requestImages Observable"))
                .flatMap(venueResponse -> {
                    venueResponse.getResponse().getVenue().getLocation().setDistance(currentDistance);
                    return Observable.just(venueResponse);
                })
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY)))
                .subscribe(venueResponse -> presenterListener.onRequestImages(venueResponse, null),
                        error -> presenterListener.onRequestImages(null, error.getMessage()));
    }
}
