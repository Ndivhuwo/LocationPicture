package com.smartalgorithms.locationpictures.Venue;

import com.smartalgorithms.locationpictures.Dagger.Annotations;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;

import javax.inject.Provider;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */
@Module
public abstract class VenueModel {

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
    static VenuePresenter provideVenuePresenter(VenueContract.ViewListener viewListener, Provider<VenueAdapter> venueAdapterProvider, GeneralHelper generalHelper) {
        return new VenuePresenter(viewListener, venueAdapterProvider, generalHelper);
    }

    @Annotations.ActivityScope
    @Provides
    static VenueAdapter provideVenueAdapter(GeneralHelper generalHelper) {
        return new VenueAdapter(generalHelper);
    }

    @Annotations.ActivityScope
    @Binds
    abstract VenueContract.ViewListener provideViewListener(VenueActivity venueActivity);
}
