package com.smartalgorithms.locationpictures.MapMarker;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.smartalgorithms.locationpictures.Constants;
import com.smartalgorithms.locationpictures.Helpers.DialogHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.R;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/29.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/29.
 */

public class MapMarkerActivity extends AppCompatActivity implements MapMarkerContact.ViewListener, ViewModelProvider.Factory{
    private static final String TAG = MapMarkerActivity.class.getSimpleName();
    private MapMarkerPresenter presenter;
    @Inject Provider<MapMarkerPresenter> presenterProvider;
    @Inject DialogHelper dialogHelper;
    @Inject ResourcesHelper resourcesHelper;
    @Inject Intent intent;

    @BindView(R.id.mv_map) MapView mv_map;
    @BindView(R.id.lav_loading) LottieAnimationView lav_loading;
    @BindView(R.id.rlyt_loading) RelativeLayout rlyt_loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        presenter = ViewModelProviders.of(this, this).get(MapMarkerPresenter.class);
        setupUI(savedInstanceState);
    }

    @Override
    protected void onResume() {
        mv_map.onResume();
        mv_map.getMapAsync(googleMap -> presenter.onMapReady(googleMap));
        super.onResume();
        presenter.resume(getIntent().getStringArrayListExtra(Constants.INTENT_EXTRA_PLACE_LIST),
                getIntent().getDoubleExtra(Constants.INTENT_EXTRA_CURRENT_LAT, -1),
                getIntent().getDoubleExtra(Constants.INTENT_EXTRA_CURRENT_LNG, -1));
    }

    private void setupUI(Bundle savedInstanceState) {
        setContentView(R.layout.activity_map_marker);
        ButterKnife.bind(this);
        mv_map.onCreate(savedInstanceState);
    }

    @Override
    public void displayMessage(String title, String message) {
        dialogHelper.showCustomAlertDialog(this, title, message, null, null, false, resourcesHelper.getString(R.string.text_ok), null);
    }

    @Override
    public void transitionOn(Class<?> toClass, Bundle bundle, boolean finish) {
        intent.setClass(MapMarkerActivity.this, toClass);
        if(bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
        if(finish)
            finishActivity();
    }

    public void finishActivity() {
        finish();
    }

    @Override
    public void displayLoadingLottieAnimation(boolean show) {
        if(show){
            rlyt_loading.setVisibility(View.VISIBLE);
            lav_loading.setVisibility(View.VISIBLE);
            lav_loading.setAnimation("working.json");
            lav_loading.playAnimation();
        }else {
            lav_loading.setVisibility(View.GONE);
            rlyt_loading.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(presenterProvider.get());
    }
}
