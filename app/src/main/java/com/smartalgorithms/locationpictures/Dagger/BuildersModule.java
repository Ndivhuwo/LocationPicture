package com.smartalgorithms.locationpictures.Dagger;

import com.smartalgorithms.locationpictures.Home.HomeActivity;
import com.smartalgorithms.locationpictures.Home.HomeModule;
import com.smartalgorithms.locationpictures.LocationPictures.LocationPicturesActivity;
import com.smartalgorithms.locationpictures.LocationPictures.LocationPicturesModule;
import com.smartalgorithms.locationpictures.MapMarker.MapMarkerActivity;
import com.smartalgorithms.locationpictures.MapMarker.MapMarkerModule;
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

    @Annotations.ActivityScope
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity bindHomeActivity();

    @Annotations.ActivityScope
    @ContributesAndroidInjector(modules = LocationPicturesModule.class)
    abstract LocationPicturesActivity bindLocationPicturesActivity();

    @Annotations.ActivityScope
    @ContributesAndroidInjector(modules = VenueModel.class)
    abstract VenueActivity bindVenueActivity();

    @Annotations.ActivityScope
    @ContributesAndroidInjector(modules = MapMarkerModule.class)
    abstract MapMarkerActivity bindMapMarkerActivity();
}
