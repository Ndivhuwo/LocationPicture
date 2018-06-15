package com.smartalgorithms.locationpictures.Home;

import android.arch.lifecycle.LifecycleOwner;
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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */
@Module
public abstract class HomeModule {
    @Annotations.ActivityScope
    @Provides
    static HomePresenter provideHomePresenter(LifecycleOwner lifecycleOwner, HomeContract.ViewListener viewListener, Provider<HomeUseCase> homeUseCaseProvider,
                                              RxPermissions rxPermissions, ResourcesHelper resourcesHelper, GeneralHelper generalHelper,
                                              LoggingHelper loggingHelper, Provider<HomePlaceAdapter> homePlaceAdapterProvider) {
        return new HomePresenter(lifecycleOwner, rxPermissions, homeUseCaseProvider, viewListener, resourcesHelper, generalHelper, loggingHelper, homePlaceAdapterProvider);
    }

    @Annotations.ActivityScope
    @Provides
    static HomeViewModel provideHomeViewModel(HomeContract.ViewListener viewListener, Provider<HomeUseCase> homeUseCaseProvider,
                                              RxPermissions rxPermissions, ResourcesHelper resourcesHelper, GeneralHelper generalHelper,
                                              LoggingHelper loggingHelper, Provider<HomePlaceAdapter> homePlaceAdapterProvider, Provider<CompositeDisposable> compositeDisposableProvider) {
        return new HomeViewModel(rxPermissions, homeUseCaseProvider, viewListener, resourcesHelper, generalHelper, loggingHelper, homePlaceAdapterProvider, compositeDisposableProvider);
    }

    @Annotations.ActivityScope
    @Provides
    static HomeUseCase provideHomeUseCase(LifecycleOwner lifecycleOwner, HomeContract.PresenterListener presenterListener, Provider<Scheduler> subscribeSchedulerProvider, LocationNetAPI locationNetAPI,
                                          LocationResponse locationResponse, NearByPlacesNetAPI nearByPlacesNetAPI, NearByPlacesResponse nearByPlacesResponse, LoggingHelper loggingHelper) {
        return new HomeUseCase(lifecycleOwner, presenterListener, subscribeSchedulerProvider, AndroidSchedulers.mainThread(), locationNetAPI, locationResponse, nearByPlacesNetAPI, nearByPlacesResponse, loggingHelper);
    }

    @Annotations.ActivityScope
    @Provides
    static CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Annotations.ActivityScope
    @Annotations.SubscribeScheduler
    @Provides
    static Scheduler provideSubscribeScheduler() {
        return Schedulers.computation();
    }

    @Annotations.ActivityScope
    @Annotations.ObserveScheduler
    @Provides
    static Scheduler provideObserveScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Annotations.ActivityScope
    @Provides
    static HomePlaceAdapter provideHomePlaceAdapter(HomeContract.AdapterPresenterListener listener, Bundle bundle) {
        return new HomePlaceAdapter(bundle, listener);
    }

    @Annotations.ActivityScope
    @Provides
    static RxPermissions providesRxPermissions(HomeActivity homeActivity) {
        return new RxPermissions(homeActivity);
    }

    @Annotations.ActivityScope
    @Provides
    @Annotations.ActivityContext
    static Context provideActivityContext(HomeActivity homeActivity) {
        return homeActivity;
    }

    @Annotations.ActivityScope
    @Provides
    static LocalBroadcastManager provideLocalBroadcastManager(@Annotations.ActivityContext Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

    @Annotations.ActivityScope
    @Binds
    abstract HomeContract.PresenterListener providePresenterListener(HomeViewModel homeViewModel);

    @Annotations.ActivityScope
    @Binds
    abstract LifecycleOwner provideLifecycleOwner(HomeActivity homeActivity);

    @Annotations.ActivityScope
    @Binds
    abstract HomeContract.AdapterPresenterListener provideAdapterPresenterListener(HomePresenter homePresenter);

    @Annotations.ActivityScope
    @Binds
    abstract HomeContract.ViewListener provideViewListener(HomeActivity homeActivity);
}
