package com.smartalgorithms.locationpictures.Venue;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class VenueContract {
    public interface ViewListener {
        void setVenueAdapter(VenueAdapter venueAdapter);
        void setInfo(String name, String address);
    }
}
