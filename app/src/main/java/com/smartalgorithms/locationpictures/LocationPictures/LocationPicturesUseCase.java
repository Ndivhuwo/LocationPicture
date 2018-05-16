package com.smartalgorithms.locationpictures.LocationPictures;

import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.Network.NearByPlacesNetAPI;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

    @Inject
    public LocationPicturesUseCase(LocationPicturesContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider, Scheduler observeScheduler, NearByPlacesNetAPI nearByPlacesNetAPI, GeneralHelper generalHelper) {
        this.presenterListener = presenterListener;
        this.subscribeSchedulerProvider = subscribeSchedulerProvider;
        this.observeScheduler = observeScheduler;
        this.nearByPlacesNetAPI = nearByPlacesNetAPI;
        this.generalHelper = generalHelper;
    }

    public void resume(ArrayList<String> stringArrayListExtra) {
        requestImages(stringArrayListExtra);
    }

    private void requestImages(ArrayList<String> stringArrayListExtra) {
        Observable.timer(100, TimeUnit.MILLISECONDS)
                .flatMap(time -> Observable.fromIterable(stringArrayListExtra)
                        .map(venueJSONString -> {
                            NearByPlacesResponse.Response.Group.Item.Venue venue = (NearByPlacesResponse.Response.Group.Item.Venue) generalHelper.getObjectFromJson(venueJSONString, NearByPlacesResponse.Response.Group.Item.Venue.class);
                            currentDistance = venue.getLocation().getDistance();
                            return nearByPlacesNetAPI.getVenueDetails(venue.getId());
                        })
                        .flatMap(venueResponse -> {
                            venueResponse.getResponse().getVenue().getLocation().setDistance(currentDistance);
                            return Observable.just(venueResponse);
                        })
                        .subscribeOn(subscribeSchedulerProvider.get())
                        .toList()
                        .toObservable())
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .subscribe(venueResponseList -> presenterListener.onRequestImages(venueResponseList, null),
                        error -> presenterListener.onRequestImages(null, error.getMessage()));
    }
}
