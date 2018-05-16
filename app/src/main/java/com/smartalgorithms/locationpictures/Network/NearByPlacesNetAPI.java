package com.smartalgorithms.locationpictures.Network;

import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.Models.VenueResponse;
import com.smartalgorithms.locationpictures.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class NearByPlacesNetAPI {
    private static final String TAG = NearByPlacesNetAPI.class.getSimpleName();

    private NetworkUseCase networkUseCase;
    private LoggingHelper loggingHelper;
    private ResourcesHelper resourcesHelper;
    private GeneralHelper generalHelper;
    private NearByPlacesResponse nearByPlacesResponse;
    private VenueResponse venueResponse;

    @Inject
    public NearByPlacesNetAPI(NetworkUseCase networkUseCase, LoggingHelper loggingHelper, ResourcesHelper resourcesHelper, GeneralHelper generalHelper, NearByPlacesResponse nearByPlacesResponse, VenueResponse venueResponse) {
        this.networkUseCase = networkUseCase;
        this.loggingHelper = loggingHelper;
        this.resourcesHelper = resourcesHelper;
        this.generalHelper = generalHelper;
        this.nearByPlacesResponse = nearByPlacesResponse;
        this.venueResponse = venueResponse;
    }

    private NearByPlacesResponse getNearByPlaces(LatLng coordinates, int radiusMeters){
        loggingHelper.i(TAG, "getNearByPlaces");

        loggingHelper.i(TAG, "current coordinates : lat: " + coordinates.latitude + " long: " + coordinates.longitude);
        final HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(resourcesHelper.getString(R.string.foursquare_url))
                .addPathSegments("v2/venues/explore")
                .addQueryParameter("ll", coordinates.latitude + "," + coordinates.longitude)
                .addQueryParameter("radius", radiusMeters+"")
                .addQueryParameter("client_id", resourcesHelper.getString(R.string.foursquare_client_id))
                .addQueryParameter("client_secret", resourcesHelper.getString(R.string.foursquare_client_secret))
                .addQueryParameter("v", new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .addQueryParameter("limit", 500+"")
                .build();

        Response response = networkUseCase.networkGET(url);
        try {
            if (response.isSuccessful()){
                loggingHelper.i(TAG, "getNearByPlaces success");
                nearByPlacesResponse = (NearByPlacesResponse) generalHelper.getObjectFromJson(response.body().string(), NearByPlacesResponse.class);
                nearByPlacesResponse.setSuccess(true);
            }else {
                loggingHelper.i(TAG, "getNearByPlaces failed: "+ response.body().string());
                nearByPlacesResponse.setSuccess(false);
                nearByPlacesResponse.setMessage(response.body().string());
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            nearByPlacesResponse.setSuccess(false);
            nearByPlacesResponse.setMessage(ioe.getMessage() + " " + ioe.getCause());
        }

        return nearByPlacesResponse;
    }

    public VenueResponse getVenueDetails(String id){
        loggingHelper.i(TAG, "getVenueDetails, venue id: " + id);

        final HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(resourcesHelper.getString(R.string.foursquare_url))
                .addPathSegments("v2/venues")
                .addPathSegment(id)
                .addQueryParameter("client_id", resourcesHelper.getString(R.string.foursquare_client_id))
                .addQueryParameter("client_secret", resourcesHelper.getString(R.string.foursquare_client_secret))
                .addQueryParameter("v", new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .build();

        Response response = networkUseCase.networkGET(url);
        try {
            if (response.isSuccessful()){
                loggingHelper.i(TAG, "getVenueDetails success");
                venueResponse = (VenueResponse) generalHelper.getObjectFromJson(response.body().string(), VenueResponse.class);
                venueResponse.setSuccess(true);
            }else {
                loggingHelper.i(TAG, "getVenueDetails failed: "+ response.body().string());
                venueResponse.setSuccess(false);
                venueResponse.setMessage(response.body().string());
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            venueResponse.setSuccess(false);
            venueResponse.setMessage(ioe.getMessage() + " " + ioe.getCause());
        }

        return venueResponse;
    }

    public Observable<NearByPlacesResponse> getNearByPlacesSingle(LatLng coordinates, int radiusMeters) {
        return Observable.defer(() -> Observable.just(getNearByPlaces(coordinates, radiusMeters)));
    }

    public Observable<VenueResponse> getVenueDetailsSingle(String id) {
        return Observable.defer(() -> Observable.just(getVenueDetails(id)));
    }
}
