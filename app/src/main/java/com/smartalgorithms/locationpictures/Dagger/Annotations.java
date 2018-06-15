package com.smartalgorithms.locationpictures.Dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Scope;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class Annotations {
    @Qualifier
    public @interface ApplicationContext {}
    @Qualifier
    public @interface  ActivityContext {}

    @Qualifier
    public @interface SubscribeScheduler {}

    @Qualifier
    public @interface ObserveScheduler {}

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ActivityScope {}
}
