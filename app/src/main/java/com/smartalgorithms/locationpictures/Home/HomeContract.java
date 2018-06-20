package com.smartalgorithms.locationpictures.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class HomeContract {
    public interface ViewListener {
        void displayMessage(String title, String message);
        void displayMessage(String title, String message, View.OnClickListener okListener, View.OnClickListener cancelListener, String okText, String cancelText);
        void togglePermissions(boolean permissionsSet);
        void transitionOn(Class<?> toClass, Bundle bundle, boolean finish);
        void finishActivity();
        void displayToast(String message);
        void updateCurrentLocation(String address);
        void displayReloadLottieAnimation(boolean show);
        void displayLoadingLottieAnimation(boolean show);
        void onAdapterCreated(HomePlaceAdapter adapter);
        void requestLocation();
    }

    public interface PresenterListener {
        void requestPhonePermissions();
        void onGetAddress(LocationResponse reverseGeoResponse);
        void onGetNearByPlaces(@Nullable NearByPlacesResponse nearByPlacesResponse, @Nullable String message);
        void displayReloadLottieAnimation(boolean show);
        void requestNearByPlaces(LatLng coordinates, int searchRadius);
        void requestAddress(LatLng coordinates);
        void requestLocation();
    }

    public interface AdapterPresenterListener {
        void transitionOn(Class<?> toClass, Bundle bundle, boolean finish);
    }
}
