package com.smartalgorithms.locationpictures.MapMarker;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/29.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/29.
 */

public class MapMarkerUseCase {
    private static final String TAG = MapMarkerUseCase.class.getSimpleName();
    private MapMarkerContact.PresenterListener presenterListener;
    private Provider<Scheduler> subscribeSchedulerProvider;
    private Scheduler observeScheduler;
    private GeneralHelper generalHelper;
    private LifecycleOwner lifecycleOwner;
    private LoggingHelper loggingHelper;

    @Inject
    public MapMarkerUseCase(LifecycleOwner lifecycleOwner, MapMarkerContact.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider, Scheduler observeScheduler, GeneralHelper generalHelper, LoggingHelper loggingHelper) {
        this.lifecycleOwner = lifecycleOwner;
        this.presenterListener = presenterListener;
        this.subscribeSchedulerProvider = subscribeSchedulerProvider;
        this.observeScheduler = observeScheduler;
        this.generalHelper = generalHelper;
        this.loggingHelper = loggingHelper;
    }

    public void resume(ArrayList<String> locationList) {
        getLatLangList(locationList);
    }

    private void getLatLangList(ArrayList<String> locationList) {
        Observable.timer(100, TimeUnit.MILLISECONDS)
                .flatMap(time -> Observable.fromIterable(locationList)
                        .map(venueJSONString -> {
                            NearByPlacesResponse.Response.Group.Item.Venue venue = (NearByPlacesResponse.Response.Group.Item.Venue) generalHelper.getObjectFromJson(venueJSONString, NearByPlacesResponse.Response.Group.Item.Venue.class);
                            return venue;
                        })
                        .subscribeOn(subscribeSchedulerProvider.get())
                        .toList()
                        .toObservable()
                )
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing getLatLangList Observable"))
                .subscribeOn(subscribeSchedulerProvider.get())
                .observeOn(observeScheduler)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY)))
                .subscribe(venues -> presenterListener.requestPopulateMap(venues));
    }
}
