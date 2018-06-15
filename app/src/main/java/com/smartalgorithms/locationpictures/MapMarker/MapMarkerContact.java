package com.smartalgorithms.locationpictures.MapMarker;

import android.os.Bundle;

import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;

import java.util.List;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/29.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/29.
 */

public class MapMarkerContact {
    public interface ViewListener {
        void displayMessage(String title, String message);
        void transitionOn(Class<?> toClass, Bundle bundle, boolean finish);
        void displayLoadingLottieAnimation(boolean show);
    }

    public interface PresenterListener {
        void requestPopulateMap(List<NearByPlacesResponse.Response.Group.Item.Venue> venues);
    }
}
