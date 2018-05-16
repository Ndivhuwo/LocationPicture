package com.smartalgorithms.locationpictures.Venue;

import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Models.VenueResponse;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class VenuePresenter {
    private static final String TAG = VenuePresenter.class.getSimpleName();
    private VenueContract.ViewListener viewListener;
    private Provider<VenueAdapter> venueAdapterProvider;
    private GeneralHelper generalHelper;

    @Inject
    public VenuePresenter(VenueContract.ViewListener viewListener, Provider<VenueAdapter> venueAdapterProvider, GeneralHelper generalHelper) {
        this.viewListener = viewListener;
        this.venueAdapterProvider = venueAdapterProvider;
        this.generalHelper = generalHelper;
    }

    public void setVenue(String stringExtra) {
        VenueAdapter venueAdapter = venueAdapterProvider.get();
        VenueResponse.Response.Venue venue = (VenueResponse.Response.Venue) generalHelper.getObjectFromJson(stringExtra, VenueResponse.Response.Venue.class);
        String address = "";
        if(venue.getLocation().getAddress() != null)
            address += venue.getLocation().getAddress() + " ";
        if(venue.getLocation().getCrossStreet() != null)
            address+= venue.getLocation().getCrossStreet();
        viewListener.setInfo(venue.getName(), address);
        venueAdapter.setPhotos(venue.getPhotos().getGroups().get(0).getItems());
        viewListener.setVenueAdapter(venueAdapter);

    }
}
