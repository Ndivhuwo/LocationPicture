package com.smartalgorithms.locationpictures.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.smartalgorithms.locationpictures.App;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static String TAG = LocationService.class.getSimpleName();
    private GoogleApiClient googleApiClient;
    private Context context;
    private LocationRequest locationRequest;
    @Inject LoggingHelper loggingHelper;
    @Inject GeneralHelper generalHelper;
    @Inject Intent intent;

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                if (generalHelper.isLocationEnabled()) {
                    unregisterReceiver(this);
                    startLocationUpdates();
                }
            }
        }
    };

    private void startLocationUpdates() {
        loggingHelper.d(TAG, "startLocationUpdates");
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
    }

    private void stopLocationUpdates() {
        loggingHelper.d(TAG, "stopLocationUpdates");
        googleApiClient.disconnect();
    }

    @SuppressWarnings("MissingPermission")
    private void requestLocationUpdates() {
        loggingHelper.d(TAG, "requestLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, LocationService.this);
    }

    private boolean checkPermissionLocation() {
        loggingHelper.d(TAG, "checkPermissionLocation");
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        loggingHelper.d(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((App) getApplicationContext()).getDaggerAppComponent().inject(this);
        loggingHelper.d(TAG, "onStartCommand");
        this.context = this;
        startLocationUpdates();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(Location location) {
        loggingHelper.d(TAG, "onLocationChanged");
        intent.setAction("com.smartalgorithms.locationpictures");
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        loggingHelper.d(TAG, "onConnected");
        Location lastLocation = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (lastLocation != null)
            onLocationChanged(lastLocation);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        if (checkPermissionLocation() && generalHelper.isLocationEnabled()) {
            requestLocationUpdates();
        } else {
            registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
