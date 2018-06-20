package com.smartalgorithms.locationpictures.MapMarker;

import android.arch.lifecycle.LiveData;

import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/29.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/29.
 */

public class MapMarkerUseCase extends LiveData<String>{
    private static final String TAG = MapMarkerUseCase.class.getSimpleName();
    private MapMarkerContact.PresenterListener presenterListener;
    private Provider<Scheduler> subscribeSchedulerProvider;
    private Scheduler observeScheduler;
    private GeneralHelper generalHelper;
    private LoggingHelper loggingHelper;
    private CompositeDisposable compositeDisposable;
    private Provider<CompositeDisposable> compositeDisposableProvider;

    @Inject
    public MapMarkerUseCase(MapMarkerContact.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider, Scheduler observeScheduler, GeneralHelper generalHelper, LoggingHelper loggingHelper,
                            Provider<CompositeDisposable> compositeDisposableProvider) {
        this.presenterListener = presenterListener;
        this.subscribeSchedulerProvider = subscribeSchedulerProvider;
        this.observeScheduler = observeScheduler;
        this.generalHelper = generalHelper;
        this.loggingHelper = loggingHelper;
        this.compositeDisposableProvider = compositeDisposableProvider;
    }

    public void resume(ArrayList<String> locationList) {
        getLatLangList(locationList);
    }

    @Override
    protected void onInactive() {
        if(compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onInactive();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposableProvider != null) {
            if (compositeDisposable == null || compositeDisposable.isDisposed())
                compositeDisposable = compositeDisposableProvider.get();
        }
        return compositeDisposable;
    }

    private void getLatLangList(ArrayList<String> locationList) {
        getCompositeDisposable().add(Observable.timer(100, TimeUnit.MILLISECONDS)
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
                .subscribe(venues -> presenterListener.requestPopulateMap(venues)));
    }
}
