package com.smartalgorithms.locationpictures.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.smartalgorithms.locationpictures.Dagger.Annotations;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.Models.LocationResponse;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.Network.LocationNetAPI;
import com.smartalgorithms.locationpictures.Network.NearByPlacesNetAPI;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Provider;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */
@Module
public abstract class HomeModule {
    @Provides
    static HomePresenter provideHomePresenter(HomeContract.ViewListener viewListener, Provider<HomeUseCase> homeUseCaseProvider,
                                              RxPermissions rxPermissions, ResourcesHelper resourcesHelper, GeneralHelper generalHelper,
                                              LoggingHelper loggingHelper, Provider<HomePlaceAdapter> homePlaceAdapterProvider) {
        return new HomePresenter(rxPermissions, homeUseCaseProvider, viewListener, resourcesHelper, generalHelper, loggingHelper, homePlaceAdapterProvider);
    }

    @Binds
    abstract HomeContract.PresenterListener providePresenterListener(HomePresenter homePresenter);

    @Provides
    static HomeUseCase provideHomeUseCase(HomeContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider, LocationNetAPI locationNetAPI,
                                          LocationResponse locationResponse, NearByPlacesNetAPI nearByPlacesNetAPI, NearByPlacesResponse nearByPlacesResponse) {
        return new HomeUseCase(presenterListener, subscribeSchedulerProvider, AndroidSchedulers.mainThread(), locationNetAPI, locationResponse, nearByPlacesNetAPI, nearByPlacesResponse);
    }

    @Binds
    abstract HomeContract.AdapterPresenterListener provideAdapterPresenterListener(HomePresenter homePresenter);

    @Provides
    static HomePlaceAdapter provideHomePlaceAdapter(HomeContract.AdapterPresenterListener listener, Bundle bundle) {
        return new HomePlaceAdapter(bundle, listener);
    }

    @Provides
    static RxPermissions providesRxPermissions(HomeActivity homeActivity){
        return new RxPermissions(homeActivity);
    }

    @Binds
    abstract HomeContract.ViewListener provideViewListener(HomeActivity homeActivity);

    @Provides
    @Annotations.ActivityContext
    static Context provideActivityContext(HomeActivity homeActivity){
        return homeActivity;
    }

    @Provides
    static LocalBroadcastManager provideLocalBroadcastManager(@Annotations.ActivityContext Context context) {
        return LocalBroadcastManager.getInstance(context);
    }
}
