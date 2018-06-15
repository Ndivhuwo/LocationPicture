package com.smartalgorithms.locationpictures.LocationPictures;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.smartalgorithms.locationpictures.Constants;
import com.smartalgorithms.locationpictures.CustomViews.GridViewWrapHeight;
import com.smartalgorithms.locationpictures.Helpers.DialogHelper;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.Helpers.ResourcesHelper;
import com.smartalgorithms.locationpictures.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/15.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/15.
 */

public class LocationPicturesActivity extends AppCompatActivity implements LocationPicturesContract.ViewListener{
    private static final String TAG = LocationPicturesActivity.class.getSimpleName();
    private boolean loadImages = true;
    @Inject LocationPicturesPresenter presenter;
    @Inject Intent intent;
    @Inject DialogHelper dialogHelper;
    @Inject ResourcesHelper resourcesHelper;
    @Inject LoggingHelper loggingHelper;

    @BindView(R.id.tv_header) TextView tv_header;
    @BindView(R.id.rv_images) RecyclerView rv_images;
    @BindView(R.id.tv_message) TextView tv_message;
    @BindView(R.id.rlyt_loading) RelativeLayout rlyt_loading;
    @BindView(R.id.lav_loading) LottieAnimationView lav_loading;
    @BindView(R.id.tv_loading) TextView tv_loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        loadImages = true;
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loadImages) {

            presenter.resume(getIntent().getStringExtra(Constants.INTENT_EXTRA_PLACE));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadImages = false;
    }

    private void setupUI() {
        setContentView(R.layout.activity_location_pictures);
        ButterKnife.bind(this);
        rv_images.setNestedScrollingEnabled(false);
        rv_images.setLayoutManager(new GridLayoutManager(LocationPicturesActivity.this, 2));
    }

    @Override
    public void updateTitle(String location) {
        tv_header.setText(location);
    }

    @Override
    public void displayMessage(int title, int message) {
        dialogHelper.showCustomAlertDialog(this, resourcesHelper.getString(title), resourcesHelper.getString(message), null, null, false, resourcesHelper.getString(R.string.text_ok), null);
    }

    @Override
    public void displayLoadingLottieAnimation(boolean show) {
        if(show){
            rlyt_loading.setVisibility(View.VISIBLE);
            lav_loading.setVisibility(View.VISIBLE);
            lav_loading.setAnimation("cube_loader.json");
            lav_loading.playAnimation();
        }else {
            lav_loading.setVisibility(View.GONE);
            rlyt_loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void transitionOn(Class<?> toClass, Bundle bundle, boolean finish) {
        intent.setClass(LocationPicturesActivity.this, toClass);
        if(bundle != null)
            intent.putExtras(bundle);

        if(finish) {
            startActivity(intent);
            finishActivity();
        }else
            startActivityForResult(intent, Constants.REQUEST_CODE_VENUE);
    }

    @Override
    public void setGridViewAdapter(@Nullable LocationPicturesAdapter locationPicturesAdapter) {
        if(locationPicturesAdapter != null) {
            rv_images.setAdapter(locationPicturesAdapter);
            tv_message.setVisibility(View.GONE);
        }
        else
            tv_message.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_CODE_VENUE) {
            loggingHelper.i(TAG, "Return from VenueActivity");
            loadImages = false;
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        finish();
    }
}
