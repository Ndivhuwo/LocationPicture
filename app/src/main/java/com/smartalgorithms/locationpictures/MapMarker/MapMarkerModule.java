package com.smartalgorithms.locationpictures.MapMarker;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;

import com.smartalgorithms.locationpictures.Dagger.Annotations;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;

import javax.inject.Provider;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/29.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/29.
 */

@Module
public abstract class MapMarkerModule {

    @Annotations.ActivityScope
    @Provides
    static MapMarkerPresenter provideMapMarkerPresenter(LifecycleOwner lifecycleOwner, MapMarkerContact.ViewListener viewListener, Provider<MapMarkerUseCase> mapMarkerUseCaseProvider,
                                                        GeneralHelper generalHelper, ResourcesHelper resourcesHelper, Bundle bundle, LoggingHelper loggingHelper) {
        return new MapMarkerPresenter(lifecycleOwner, viewListener, mapMarkerUseCaseProvider, resourcesHelper, generalHelper, bundle, loggingHelper, Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    @Annotations.ActivityScope
    @Provides
    static MapMarkerUseCase provideMapMarkerUseCase(LifecycleOwner lifecycleOwner, MapMarkerContact.PresenterListener presenterListener,
                                                    Provider<Scheduler> subscribeSchedulerProvider, GeneralHelper generalHelper, LoggingHelper loggingHelper) {
        return new MapMarkerUseCase(lifecycleOwner, presenterListener, subscribeSchedulerProvider, AndroidSchedulers.mainThread(), generalHelper, loggingHelper);
    }

    @Annotations.ActivityScope
    @Binds
    abstract MapMarkerContact.ViewListener provideViewListener(MapMarkerActivity mapMarkerActivity);

    @Annotations.ActivityScope
    @Binds
    abstract MapMarkerContact.PresenterListener providePresenterListener(MapMarkerPresenter mapMarkerPresenter);

    @Annotations.ActivityScope
    @Binds
    abstract LifecycleOwner provideLifecycleOwner(MapMarkerActivity mapMarkerActivity);
}
