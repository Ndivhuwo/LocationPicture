package com.smartalgorithms.locationpictures.LocationPictures;

import android.os.Bundle;

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
    @Provides
    static LocationPicturesPresenter provideLocationPicturesPresenter(LocationPicturesContract.ViewListener viewListener, Provider<LocationPicturesUseCase> locationPicturesUseCaseProvider,
                                                                      Provider<LocationPicturesAdapter> locationPicturesAdapter, LoggingHelper loggingHelper) {
        return new LocationPicturesPresenter(viewListener, locationPicturesUseCaseProvider, locationPicturesAdapter, loggingHelper);
    }

    @Binds
    abstract LocationPicturesContract.ViewListener provideViewListener(LocationPicturesActivity locationPicturesActivity);

    @Provides
    static LocationPicturesUseCase provideLocationPicturesUseCase(LocationPicturesContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider, NearByPlacesNetAPI nearByPlacesNetAPI, GeneralHelper generalHelper) {
        return new LocationPicturesUseCase(presenterListener, subscribeSchedulerProvider, AndroidSchedulers.mainThread(), nearByPlacesNetAPI, generalHelper);
    }

    @Binds
    abstract LocationPicturesContract.PresenterListener providePresenterListener(LocationPicturesPresenter locationPicturesPresenter);

    @Provides
    static LocationPicturesAdapter provideLocationPicturesAdapter(LocationPicturesContract.AdapterPresenterListener listener, Bundle bundle, GeneralHelper generalHelper) {
        return new LocationPicturesAdapter(bundle, listener, generalHelper);
    }

    @Binds
    abstract LocationPicturesContract.AdapterPresenterListener provideAdapterPresenterListener(LocationPicturesPresenter locationPicturesPresenter);
}
