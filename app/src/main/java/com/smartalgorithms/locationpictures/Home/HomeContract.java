package com.smartalgorithms.locationpictures.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class HomeContract {
    public interface ViewListener {
        void displayMessage(String title, String message);
        void togglePermissions(boolean permissionsSet);
        void transitionOn(Class<?> toClass, Bundle bundle, boolean finish);
        void displayToast(String message);
        void requestLocation();
        void updateCurrentLocation(String address);
        void displayReloadLottieAnimation(boolean show);
        void displayLoadingLottieAnimation(boolean show);
        void onAdapterCreated(HomePlaceAdapter adapter);
    }

    public interface PresenterListener {
        void requestPhonePermissions();
        void onGetAddress(LocationResponse reverseGeoResponse);
        void onGetNearByPlaces(@Nullable NearByPlacesResponse nearByPlacesResponse, @Nullable String message);
    }

    public interface AdapterPresenterListener {
        void transitionOn(Class<?> toClass, Bundle bundle, boolean finish);
    }
}
