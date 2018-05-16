package com.smartalgorithms.locationpictures.Dagger;

import com.smartalgorithms.locationpictures.App;
import com.smartalgorithms.locationpictures.Services.LocationService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BuildersModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        Builder appModule(AppModule appModule);
        @BindsInstance
        Builder application(App application);
        AppComponent build();
    }
    void inject(App app);
    void inject(LocationService locationService);
}
