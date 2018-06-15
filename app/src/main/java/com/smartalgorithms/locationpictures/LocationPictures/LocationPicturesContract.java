package com.smartalgorithms.locationpictures.LocationPictures;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartalgorithms.locationpictures.Models.VenueResponse;

import java.util.List;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class LocationPicturesContract {
    public interface ViewListener {
        void displayMessage(int title, int message);
        void displayLoadingLottieAnimation(boolean show);
        void transitionOn(Class<?> toClass, Bundle bundle, boolean finish);
        void setGridViewAdapter(@Nullable LocationPicturesAdapter locationPicturesAdapter);
        void updateTitle(String location);
    }

    public interface PresenterListener {
        void onRequestImages(@Nullable VenueResponse venueResponseList, @Nullable String message);
    }

    public interface AdapterPresenterListener {
        void onGridItemClick(Bundle venue);
    }
}
