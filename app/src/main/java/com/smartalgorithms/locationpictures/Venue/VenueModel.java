package com.smartalgorithms.locationpictures.Venue;

import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;

import javax.inject.Provider;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */
@Module
public abstract class VenueModel {
    @Provides
    static VenuePresenter provideVenuePresenter(VenueContract.ViewListener viewListener, Provider<VenueAdapter> venueAdapterProvider, GeneralHelper generalHelper) {
        return new VenuePresenter(viewListener, venueAdapterProvider, generalHelper);
    }

    @Binds
    abstract VenueContract.ViewListener provideViewListener(VenueActivity venueActivity);

    @Provides
    static VenueAdapter provideVenueAdapter(GeneralHelper generalHelper) {
        return new VenueAdapter(generalHelper);
    }
}
