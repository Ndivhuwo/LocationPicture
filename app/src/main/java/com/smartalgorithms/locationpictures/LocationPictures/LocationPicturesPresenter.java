package com.smartalgorithms.locationpictures.LocationPictures;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.smartalgorithms.locationpictures.Constants;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Models.VenueResponse;
import com.smartalgorithms.locationpictures.R;
import com.smartalgorithms.locationpictures.Venue.VenueActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;


/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class LocationPicturesPresenter implements LocationPicturesContract.PresenterListener, LocationPicturesContract.AdapterPresenterListener{
    private static final String TAG = LocationPicturesPresenter.class.getSimpleName();
    private LocationPicturesContract.ViewListener viewListener;
    private LocationPicturesUseCase locationPicturesUseCase;
    private Provider<LocationPicturesUseCase> locationPicturesUseCaseProvider;
    private Provider<LocationPicturesAdapter> locationPicturesAdapterProvider;
    private LoggingHelper loggingHelper;

    @Inject
    public LocationPicturesPresenter(LocationPicturesContract.ViewListener viewListener, Provider<LocationPicturesUseCase> locationPicturesUseCaseProvider,
                                     Provider<LocationPicturesAdapter> locationPicturesAdapterProvider, LoggingHelper loggingHelper) {
        this.viewListener = viewListener;
        this.locationPicturesUseCaseProvider = locationPicturesUseCaseProvider;
        this.locationPicturesAdapterProvider = locationPicturesAdapterProvider;
        this.loggingHelper = loggingHelper;
    }

    public void resume(String placeJSON) {
        viewListener.displayLoadingLottieAnimation(true);
        if(locationPicturesUseCaseProvider != null) {
            if(locationPicturesUseCase == null)
                locationPicturesUseCase = locationPicturesUseCaseProvider.get();
            locationPicturesUseCase.resume(placeJSON);
        }
    }

    @Override
    public void onRequestImages(@Nullable VenueResponse venueResponse, @Nullable String message) {
        viewListener.displayLoadingLottieAnimation(false);
        viewListener.updateTitle(venueResponse.getResponse().getVenue().getName());
        if (venueResponse != null) {
            LocationPicturesAdapter locationPicturesAdapter = locationPicturesAdapterProvider.get();
            //List<VenueResponse> finalVenueResponses = new ArrayList<>();
            //for(VenueResponse response : venueResponseList) {
                if(venueResponse.getResponse().getVenue().getPhotos().getGroups().size() > 0) {
                    loggingHelper.i(TAG, venueResponse.getResponse().getVenue().getName() + " Distance: " + venueResponse.getResponse().getVenue().getLocation().getDistance());
                    //finalVenueResponses.add(response);
                    locationPicturesAdapter.setVenueResponseList(venueResponse);
                    viewListener.setGridViewAdapter(locationPicturesAdapter);
                } else {
                    viewListener.displayMessage(R.string.text_error, R.string.text_request_images_error);
                    viewListener.setGridViewAdapter(null);
                }
            //}
        }
        else {
            viewListener.displayMessage(R.string.text_error, R.string.text_request_images_error);
            viewListener.setGridViewAdapter(null);
        }

    }

    @Override
    public void onGridItemClick(Bundle bundle) {
        loggingHelper.i(TAG, "onGridItemClick venue: " + bundle.getString(Constants.INTENT_EXTRA_PLACE));
        viewListener.transitionOn(VenueActivity.class, bundle, false);
    }
}
