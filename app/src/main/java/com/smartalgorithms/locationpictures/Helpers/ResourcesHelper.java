package com.smartalgorithms.locationpictures.Helpers;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Inject;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class ResourcesHelper {
    private Context context;

    @Inject
    public ResourcesHelper(Context context) {
        this.context = context;
    }

    public  String getString(int resourceId) {
        try {
            return context.getResources().getString(resourceId);
        } catch (Resources.NotFoundException e) {
            return "";
        }
    }
}
