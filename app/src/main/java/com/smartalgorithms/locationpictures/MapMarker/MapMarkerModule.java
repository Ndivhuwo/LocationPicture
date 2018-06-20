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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/29.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/29.
 */

@Module
public abstract class MapMarkerModule {

    @Annotations.ActivityScope
    @Provides
    static MapMarkerUseCase provideMapMarkerUseCase(MapMarkerContact.PresenterListener presenterListener,
                                                    Provider<Scheduler> subscribeSchedulerProvider, GeneralHelper generalHelper, LoggingHelper loggingHelper,
                                                    Provider<CompositeDisposable> compositeDisposableProvider) {
        return new MapMarkerUseCase(presenterListener, subscribeSchedulerProvider, AndroidSchedulers.mainThread(), generalHelper, loggingHelper, compositeDisposableProvider);
    }

    @Annotations.ActivityScope
    @Provides
    static MapMarkerPresenter provideMapMarkerPresenter(MapMarkerContact.ViewListener viewListener, Provider<MapMarkerUseCase> mapMarkerUseCaseProvider,
                                                        GeneralHelper generalHelper, ResourcesHelper resourcesHelper, Bundle bundle, LoggingHelper loggingHelper,
                                                        Provider<CompositeDisposable> compositeDisposableProvider) {
        return new MapMarkerPresenter(viewListener, mapMarkerUseCaseProvider, resourcesHelper, generalHelper, bundle, loggingHelper, Schedulers.computation(), AndroidSchedulers.mainThread(), compositeDisposableProvider);
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

    @Annotations.ActivityScope
    @Provides
    static CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
