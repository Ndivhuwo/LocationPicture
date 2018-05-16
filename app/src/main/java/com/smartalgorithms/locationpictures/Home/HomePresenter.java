package com.smartalgorithms.locationpictures.Home;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class HomePresenter implements HomeContract.PresenterListener, HomeContract.AdapterPresenterListener{
    private static final String TAG = HomePresenter.class.getSimpleName();

    private RxPermissions rxPermissions;
    private HomeUseCase homeUseCase;
    private Provider<HomeUseCase> homeUseCaseProvider;
    private HomeContract.ViewListener viewListener;
    private ResourcesHelper resourcesHelper;
    private GeneralHelper generalHelper;
    private LoggingHelper loggingHelper;
    private Provider<HomePlaceAdapter> homePlaceAdapterProvider;
    private boolean requestPermissions = true;

    @Inject
    HomePresenter(RxPermissions rxPermissions, Provider<HomeUseCase> homeUseCaseProvider,
                         HomeContract.ViewListener viewListener, ResourcesHelper resourcesHelper,
                         GeneralHelper generalHelper, LoggingHelper loggingHelper, Provider<HomePlaceAdapter> homePlaceAdapterProvider) {
        this.rxPermissions = rxPermissions;
        this.homeUseCaseProvider = homeUseCaseProvider;
        this.viewListener = viewListener;
        this.resourcesHelper = resourcesHelper;
        this.generalHelper = generalHelper;
        this.loggingHelper = loggingHelper;
        this.homePlaceAdapterProvider = homePlaceAdapterProvider;
    }

    void resume() {
        if(homeUseCaseProvider != null) {
            if(homeUseCase == null)
                homeUseCase = homeUseCaseProvider.get();
            homeUseCase.resume();
        }
    }

    @Override
    public void requestPhonePermissions() {

        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) || !rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if(requestPermissions)
                rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribe(granted -> {
                            if(granted) {
                                loggingHelper.d(TAG, "Permissions set");
                                viewListener.togglePermissions(true);
                                viewListener.requestLocation();
                            }
                            else {
                                requestPermissions = false;
                                loggingHelper.d(TAG, "Permissions not set");
                                View.OnClickListener okListener = view -> {
                                    requestPermissions = true;
                                    requestPhonePermissions();
                                };
                                View.OnClickListener cancelListener = view -> {
                                    viewListener.finishActivity();
                                };
                                viewListener.displayMessage(resourcesHelper.getString(R.string.text_error), resourcesHelper.getString(R.string.text_permissions_not_set),
                                        okListener, cancelListener, resourcesHelper.getString(R.string.text_ok), resourcesHelper.getString(R.string.text_cancel));
                                viewListener.togglePermissions(false);
                            }
                        });
        }else {
            loggingHelper.d(TAG, "Permissions Already set");
            viewListener.requestLocation();
        }
    }

    @Override
    public void onGetAddress(LocationResponse reverseGeoResponse) {
        if(reverseGeoResponse.isSuccess())
            viewListener.updateCurrentLocation(reverseGeoResponse.getResults().get(0).getFormatted_address());
        else
            loggingHelper.e(TAG, "onGetAddress Error: " + reverseGeoResponse.getMessage());
    }

    @Override
    public void onGetNearByPlaces(@Nullable NearByPlacesResponse nearByPlacesResponse, @Nullable String message) {
        if (nearByPlacesResponse != null) {
            String[] locations = {"Less than 1km Away", "Between 1km and 2km Away", "Between 2km and 3km Away", "Between 3km and 4km Away", "Between 4km and 5km Away", "Between 5km and 6km Away", "Between 6km and 7km Away"};
            LinkedHashMap<String, ArrayList<String>> venueMap = new LinkedHashMap<>();
            for (String location : locations) {
                venueMap.put(location, new ArrayList<>());
            }
            for (NearByPlacesResponse.Response.Group.Item item : nearByPlacesResponse.getResponse().getGroups().get(0).getItems()) {
                if(item.getVenue().getLocation().getDistance() <= 1000)
                    venueMap.get(locations[0]).add(generalHelper.getJSONFromObject(item.getVenue()));
                else if(item.getVenue().getLocation().getDistance() > 1000 && item.getVenue().getLocation().getDistance() <= 2000)
                    venueMap.get(locations[1]).add(generalHelper.getJSONFromObject(item.getVenue()));
                else if(item.getVenue().getLocation().getDistance() > 2000 && item.getVenue().getLocation().getDistance() <= 3000)
                    venueMap.get(locations[2]).add(generalHelper.getJSONFromObject(item.getVenue()));
                else if(item.getVenue().getLocation().getDistance() > 3000 && item.getVenue().getLocation().getDistance() <= 4000)
                    venueMap.get(locations[3]).add(generalHelper.getJSONFromObject(item.getVenue()));
                else if(item.getVenue().getLocation().getDistance() > 4000 && item.getVenue().getLocation().getDistance() <= 5000)
                    venueMap.get(locations[4]).add(generalHelper.getJSONFromObject(item.getVenue()));
                else if(item.getVenue().getLocation().getDistance() > 5000 && item.getVenue().getLocation().getDistance() <= 6000)
                    venueMap.get(locations[5]).add(generalHelper.getJSONFromObject(item.getVenue()));
                else if(item.getVenue().getLocation().getDistance() > 6000 && item.getVenue().getLocation().getDistance() <= 7000)
                    venueMap.get(locations[6]).add(generalHelper.getJSONFromObject(item.getVenue()));
                loggingHelper.i(TAG, item.getVenue().getName() + " Distance: " + item.getVenue().getLocation().getDistance());
            }

            for (Map.Entry entry : venueMap.entrySet()){
                if(((ArrayList)entry.getValue()).size() < 0)
                    venueMap.remove(entry.getKey());
            }

            HomePlaceAdapter adapter = homePlaceAdapterProvider.get();
            adapter.setVenueListMap(venueMap);
            viewListener.onAdapterCreated(adapter);
        }
        else {
            viewListener.onAdapterCreated(null);
            loggingHelper.e(TAG, "onGetNearByPlaces Error: " + message);
        }
        viewListener.displayLoadingLottieAnimation(false);
    }

    public void requestAddress(LatLng coordinates) {
        if (!generalHelper.isInternetAvailable()) {
            viewListener.displayToast(resourcesHelper.getString(R.string.error_body_internet_connection_required));
        } else
            homeUseCase.getReverseGeocode(coordinates);
    }

    public void requestNearByPlaces(LatLng coordinates, int searchRadius) {
        viewListener.displayLoadingLottieAnimation(true);
        homeUseCase.getNearByPlaces(coordinates, searchRadius);
    }

    @Override
    public void transitionOn(Class<?> toClass, Bundle bundle, boolean finish) {
        viewListener.transitionOn(toClass, bundle, finish);
    }
}
