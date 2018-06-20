package com.smartalgorithms.locationpictures.Home;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;
import com.smartalgorithms.locationpictures.App;
import com.smartalgorithms.locationpictures.Dagger.Annotations;
import com.smartalgorithms.locationpictures.Helpers.DialogHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.R;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.reactivex.Scheduler;
import io.reactivex.Single;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class HomeActivity extends AppCompatActivity implements HomeContract.ViewListener, ViewModelProvider.Factory {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private HomeViewModel homeViewModel;
    @Inject Provider<HomeViewModel> homeViewModelProvider;
    @Inject
    Intent intent;
    @Inject
    LoggingHelper loggingHelper;
    @Inject
    DialogHelper dialogHelper;
    @Inject
    ResourcesHelper resourcesHelper;
    @Inject
    @Annotations.SubscribeScheduler
    Scheduler subscribeScheduler;
    @Inject
    @Annotations.ObserveScheduler
    Scheduler observeScheduler;
    @BindView(R.id.tv_current_location)
    TextView tv_current_location;
    @BindView(R.id.iv_refresh)
    ImageView iv_refresh;
    @BindView(R.id.lav_reload)
    LottieAnimationView lav_reload;
    @BindView(R.id.lav_loading)
    LottieAnimationView lav_loading;
    @BindView(R.id.rlyt_loading)
    RelativeLayout rlyt_loading;
    @BindView(R.id.rv_places)
    RecyclerView rv_places;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.rlyt_content)
    RelativeLayout rlyt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        homeViewModel = ViewModelProviders.of(this, this).get(HomeViewModel.class);
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(App.getLocationIntent());
        homeViewModel.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(App.getLocationIntent());
    }

    @Override
    protected void onDestroy() {
        stopService(App.getLocationIntent());
        super.onDestroy();
    }

    private void setupUI() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        iv_refresh.setOnClickListener(v -> homeViewModel.onBtnClickRefresh());
    }

    @Override
    public void displayMessage(String title, String message) {
        dialogHelper.showCustomAlertDialog(this, title, message, null, null, false, resourcesHelper.getString(R.string.text_ok), null);
    }

    @Override
    public void displayMessage(String title, String message, View.OnClickListener okListener, View.OnClickListener cancelListener, String okText, String cancelText) {
        dialogHelper.showCustomAlertDialog(this, title, message, okListener, cancelListener, true, okText, cancelText);
    }

    @Override
    public void togglePermissions(boolean permissionsSet) {

    }

    @Override
    public void transitionOn(Class<?> toClass, Bundle bundle, boolean finish) {
        intent.setClass(HomeActivity.this, toClass);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
        if (finish)
            finishActivity();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void displayToast(String message) {
        dialogHelper.showToast(message);
    }

    @Override
    public void updateCurrentLocation(String address) {
        tv_current_location.setText(address);
    }

    @Override
    public void displayReloadLottieAnimation(boolean show) {
        if (show) {
            lav_reload.setVisibility(View.VISIBLE);
            iv_refresh.setVisibility(View.GONE);
            lav_reload.setAnimation("reload.json");
            lav_reload.playAnimation();
        } else {
            lav_reload.setVisibility(View.GONE);
            iv_refresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayLoadingLottieAnimation(boolean show) {
        if (show) {
            rlyt_loading.setVisibility(View.VISIBLE);
            lav_loading.setVisibility(View.VISIBLE);
            lav_loading.setAnimation("working.json");
            lav_loading.playAnimation();
        } else {
            lav_loading.setVisibility(View.GONE);
            rlyt_loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAdapterCreated(@Nullable HomePlaceAdapter adapter) {
        loggingHelper.i(TAG, "onAdapterCreated");
        if (adapter != null) {
            tv_message.setVisibility(View.GONE);
            rv_places.setAdapter(null);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HomeActivity.this);
            rv_places.setLayoutManager(mLayoutManager);
            rv_places.setItemAnimator(new DefaultItemAnimator());
            rv_places.setAdapter(adapter);
        } else {
            rv_places.setAdapter(null);
            tv_message.setText(resourcesHelper.getString(R.string.text_no_places));
            tv_message.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(homeViewModelProvider.get());
    }

    @Override
    public void requestLocation() {
        stopService(App.getLocationIntent());
        startService(App.getLocationIntent());
    }
}
