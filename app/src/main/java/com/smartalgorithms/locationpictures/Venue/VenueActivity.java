package com.smartalgorithms.locationpictures.Venue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartalgorithms.locationpictures.Constants;
import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;
import com.smartalgorithms.locationpictures.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class VenueActivity extends AppCompatActivity implements VenueContract.ViewListener{
    private static final String TAG = VenueActivity.class.getSimpleName();
    @Inject VenuePresenter presenter;
    @Inject Intent intent;
    @Inject LoggingHelper loggingHelper;
    @BindView(R.id.vp_image_swipe) ViewPager vp_image_swipe;
    @BindView(R.id.llyt_info) LinearLayout llyt_info;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_details) TextView tv_details;
    @BindView(R.id.iv_info) ImageView iv_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loggingHelper.i(TAG, "venue: " + getIntent().getStringExtra(Constants.INTENT_EXTRA_PLACE));
        presenter.setVenue(getIntent().getStringExtra(Constants.INTENT_EXTRA_PLACE));
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setupUI() {
        setContentView(R.layout.activity_venue);
        ButterKnife.bind(this);
        iv_info.setOnClickListener(viewClickListener);
        Single.timer(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time ->toggleInfo());
    }

    private ViewPager.OnClickListener viewClickListener = view -> {
        switch (view.getId()){
            case R.id.iv_info:
                toggleInfo();
        }
    };

    private void toggleInfo() {
        llyt_info.setVisibility(llyt_info.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setVenueAdapter(VenueAdapter venueAdapter) {
        vp_image_swipe.setAdapter(venueAdapter);
    }

    @Override
    public void setInfo(String name, String address) {
        tv_title.setText(name);
        tv_details.setText(address);
    }
}
