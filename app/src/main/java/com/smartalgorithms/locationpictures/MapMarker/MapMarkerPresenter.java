package com.smartalgorithms.locationpictures.MapMarker;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartalgorithms.locationpictures.Constants;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.LocationPictures.LocationPicturesActivity;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.R;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.Scheduler;
import io.reactivex.Single;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/29.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/29.
 */

public class MapMarkerPresenter implements MapMarkerContact.PresenterListener {
    private static final String TAG = MapMarkerPresenter.class.getSimpleName();

    private MapMarkerContact.ViewListener viewListener;
    private List<NearByPlacesResponse.Response.Group.Item.Venue> venues;
    private Provider<MapMarkerUseCase> mapMarkerUseCaseProvider;
    private MapMarkerUseCase mapMarkerUseCase;
    private ResourcesHelper resourcesHelper;
    private GeneralHelper generalHelper;
    private Bundle bundle;
    private GoogleMap googleMap;
    private double currentLat;
    private double currentLng;
    private LoggingHelper loggingHelper;
    private LifecycleOwner lifecycleOwner;
    private Scheduler subscribeScheduler;
    private Scheduler observeScheduler;
    private GoogleMap.OnMarkerClickListener markerClickListener = marker -> {
        Single.timer(1000, TimeUnit.MILLISECONDS)
                .doOnDispose(() -> loggingHelper.i(TAG, "Disposing timer Single"))
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY)))
                .subscribe(time -> {
                    for (NearByPlacesResponse.Response.Group.Item.Venue venue : venues) {
                        if (marker.getTag() != null && marker.getTag().equals(venue.getName())) {
                            bundle.putString(Constants.INTENT_EXTRA_PLACE, generalHelper.getJSONFromObject(venue));
                            viewListener.transitionOn(LocationPicturesActivity.class, bundle, false);
                        }
                    }
                });

        return false;
    };

    @Inject
    public MapMarkerPresenter(LifecycleOwner lifecycleOwner, MapMarkerContact.ViewListener viewListener, Provider<MapMarkerUseCase> mapMarkerUseCaseProvider,
                              ResourcesHelper resourcesHelper, GeneralHelper generalHelper, Bundle bundle,
                              LoggingHelper loggingHelper, Scheduler subscribeScheduler, Scheduler observeScheduler) {
        this.lifecycleOwner = lifecycleOwner;
        this.viewListener = viewListener;
        this.mapMarkerUseCaseProvider = mapMarkerUseCaseProvider;
        this.resourcesHelper = resourcesHelper;
        this.generalHelper = generalHelper;
        this.bundle = bundle;
        this.loggingHelper = loggingHelper;
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
    }

    public void resume(ArrayList<String> locationList, double currentLat, double currentLng) {
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        if (mapMarkerUseCaseProvider != null) {
            if (mapMarkerUseCase == null)
                mapMarkerUseCase = mapMarkerUseCaseProvider.get();
            mapMarkerUseCase.resume(locationList);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        loggingHelper.i(TAG, "onMapReady");
        this.googleMap = googleMap;
        //requestPopulateMap(venues);
    }

    @Override
    public void requestPopulateMap(List<NearByPlacesResponse.Response.Group.Item.Venue> venues) {
        this.venues = venues;
        viewListener.displayLoadingLottieAnimation(false);
        if (googleMap != null) {

            for (NearByPlacesResponse.Response.Group.Item.Venue venue : venues) {
                LatLng place = new LatLng(venue.getLocation().getLat(), venue.getLocation().getLng());
                Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(place).title(venue.getName()));
                placeMarker.setTag(venue.getName());
            }
            LatLng currentPosition = new LatLng(currentLat, currentLng);
            MarkerOptions markerOptions = new MarkerOptions().position(currentPosition);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            markerOptions.title(resourcesHelper.getString(R.string.text_my_location));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
            float zoomLevel = 12.0f;
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, zoomLevel));
            googleMap.setOnMarkerClickListener(markerClickListener);
        } else {
            //viewListener.displayMessage(resourcesHelper.getString(R.string.text_error),
            //resourcesHelper.getString(R.string.text_map_not_ready));
            viewListener.displayLoadingLottieAnimation(true);
            Single.timer(3000, TimeUnit.MILLISECONDS)
                    .doOnDispose(() -> loggingHelper.i(TAG, "Disposing timer Single"))
                    .subscribeOn(subscribeScheduler)
                    .observeOn(observeScheduler)
                    .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, Lifecycle.Event.ON_DESTROY)))
                    .subscribe(time -> requestPopulateMap(venues));
        }
    }
}
