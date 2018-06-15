package com.smartalgorithms.locationpictures.LocationPictures;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;

import com.smartalgorithms.locationpictures.Dagger.Annotations;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Network.NearByPlacesNetAPI;

import javax.inject.Provider;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

@Module
public abstract class LocationPicturesModule {
    @Annotations.ActivityScope
    @Provides
    static LocationPicturesPresenter provideLocationPicturesPresenter(LocationPicturesContract.ViewListener viewListener, Provider<LocationPicturesUseCase> locationPicturesUseCaseProvider,
                                                                      Provider<LocationPicturesAdapter> locationPicturesAdapter, LoggingHelper loggingHelper) {
        return new LocationPicturesPresenter(viewListener, locationPicturesUseCaseProvider, locationPicturesAdapter, loggingHelper);
    }

    @Annotations.ActivityScope
    @Provides
    static LocationPicturesUseCase provideLocationPicturesUseCase(LifecycleOwner lifecycleOwner, LocationPicturesContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider,
                                                                  NearByPlacesNetAPI nearByPlacesNetAPI, GeneralHelper generalHelper, LoggingHelper loggingHelper) {
        return new LocationPicturesUseCase(lifecycleOwner, presenterListener, subscribeSchedulerProvider, AndroidSchedulers.mainThread(), nearByPlacesNetAPI, generalHelper, loggingHelper);
    }

    @Annotations.ActivityScope
    @Provides
    static LocationPicturesAdapter provideLocationPicturesAdapter(LocationPicturesContract.AdapterPresenterListener listener, Bundle bundle, GeneralHelper generalHelper) {
        return new LocationPicturesAdapter(bundle, listener, generalHelper);
    }

    @Annotations.ActivityScope
    @Binds
    abstract LocationPicturesContract.ViewListener provideViewListener(LocationPicturesActivity locationPicturesActivity);

    @Annotations.ActivityScope
    @Binds
    abstract LifecycleOwner provideLifecycleOwner(LocationPicturesActivity locationPicturesActivity);

    @Annotations.ActivityScope
    @Binds
    abstract LocationPicturesContract.PresenterListener providePresenterListener(LocationPicturesPresenter locationPicturesPresenter);

    @Annotations.ActivityScope
    @Binds
    abstract LocationPicturesContract.AdapterPresenterListener provideAdapterPresenterListener(LocationPicturesPresenter locationPicturesPresenter);
}
