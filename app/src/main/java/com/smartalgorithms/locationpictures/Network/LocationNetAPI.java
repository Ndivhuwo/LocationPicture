package com.smartalgorithms.locationpictures.Network;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.R;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class LocationNetAPI {
    private static final String TAG = LocationNetAPI.class.getSimpleName();
    private NetworkUseCase networkUseCase;
    private LoggingHelper loggingHelper;
    private ResourcesHelper resourcesHelper;
    private LocationResponse locationResponse;
    private GeneralHelper generalHelper;

    @Inject
    public LocationNetAPI(NetworkUseCase networkUseCase, LoggingHelper loggingHelper, ResourcesHelper resourcesHelper, LocationResponse locationResponse, GeneralHelper generalHelper) {
        this.networkUseCase = networkUseCase;
        this.loggingHelper = loggingHelper;
        this.resourcesHelper = resourcesHelper;
        this.locationResponse = locationResponse;
        this.generalHelper = generalHelper;
    }

    private LocationResponse getAddressFromCoordinates(LatLng coordinates){
        loggingHelper.d(TAG, "addressFromCoordinates");

        loggingHelper.i(TAG, "coordinates : lat: " + coordinates.latitude + " long: " + coordinates.longitude);
        final HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(resourcesHelper.getString(R.string.google_geocode_url))
                .addPathSegments("maps/api/geocode/json")
                .addQueryParameter("latlng", coordinates.latitude + "," + coordinates.longitude)
                .addQueryParameter("key", resourcesHelper.getString(R.string.google_api_key))
                .build();

        Response response = networkUseCase.networkGET(url);
        try {
            if (response.isSuccessful()){
                loggingHelper.i(TAG, "addressFromCoordinates success");
                locationResponse = (LocationResponse) generalHelper.getObjectFromJson(response.body().string(), LocationResponse.class);
                locationResponse.setSuccess(true);
            }else {
                loggingHelper.i(TAG, "addressFromCoordinates failed: "+ response.body().string());
                locationResponse.setSuccess(false);
                locationResponse.setMessage(response.body().string());
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            locationResponse.setSuccess(false);
            locationResponse.setMessage(ioe.getMessage());
        }

        return locationResponse;
    }

    public Single<LocationResponse> getAddressFromCoordinatesSingle(LatLng coordinates) {
        return Single.defer(() -> Single.just(getAddressFromCoordinates(coordinates)));
    }
}
