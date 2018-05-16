package com.smartalgorithms.locationpictures.Helpers;

import android.util.Log;

import com.smartalgorithms.locationpictures.App;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class LoggingHelper {

    public LoggingHelper() {
    }

    public void d(String tag, String message) {
        if (App.ENABLE_LOGS) Log.d(tag, message);
    }

    public void i(String tag, String message) {
        if (App.ENABLE_LOGS) Log.i(tag, message);
    }

    public void e(String tag, String message) {
        if (App.ENABLE_LOGS) Log.e(tag, message);
    }

    public void v(String tag, String message) {
        if (App.ENABLE_LOGS) Log.v(tag, message);
    }
}
