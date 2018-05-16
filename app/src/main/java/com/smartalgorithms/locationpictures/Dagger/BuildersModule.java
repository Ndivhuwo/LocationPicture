package com.smartalgorithms.locationpictures.Dagger;

import com.smartalgorithms.locationpictures.Home.HomeActivity;
import com.smartalgorithms.locationpictures.Home.HomeModule;
import com.smartalgorithms.locationpictures.LocationPictures.LocationPicturesActivity;
import com.smartalgorithms.locationpictures.LocationPictures.LocationPicturesModule;
import com.smartalgorithms.locationpictures.Venue.VenueActivity;
import com.smartalgorithms.locationpictures.Venue.VenueModel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

@Module
public abstract class BuildersModule {
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity bindHomeActivity();

    @ContributesAndroidInjector(modules = LocationPicturesModule.class)
    abstract LocationPicturesActivity bindLocationPicturesActivity();

    @ContributesAndroidInjector(modules = VenueModel.class)
    abstract VenueActivity bindVenueActivity();
}
