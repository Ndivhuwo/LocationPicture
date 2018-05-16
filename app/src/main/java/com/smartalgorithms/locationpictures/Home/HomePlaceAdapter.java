package com.smartalgorithms.locationpictures.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartalgorithms.locationpictures.Constants;
import com.smartalgorithms.locationpictures.LocationPictures.LocationPicturesActivity;
import com.smartalgorithms.locationpictures.Models.NearByPlacesResponse;
import com.smartalgorithms.locationpictures.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/15.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/15.
 */

public class HomePlaceAdapter extends RecyclerView.Adapter<HomePlaceAdapter.ViewHolder>{
    private static final String TAG = HomePlaceAdapter.class.getSimpleName();
    private Context context;
    private LinkedHashMap<String,ArrayList<String>> venueListMap;
    private Bundle bundle;
    private HomeContract.AdapterPresenterListener listener;

    @Inject
    public HomePlaceAdapter(Bundle bundle, HomeContract.AdapterPresenterListener listener) {
        this.bundle = bundle;
        this.listener = listener;
    }

    public void setVenueListMap(LinkedHashMap<String, ArrayList<String>> venueListMap) {
        this.venueListMap = venueListMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_location, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String location = (String)venueListMap.keySet().toArray()[position];
        holder.tv_place_title.setText(location);
        holder.lyt_root.setOnClickListener(v -> {
            bundle.putStringArrayList(Constants.INTENT_EXTRA_PLACE_LIST, (ArrayList<String>) venueListMap.values().toArray()[position]);
            bundle.putString(Constants.INTENT_EXTRA_LOCATION, location);
            listener.transitionOn(LocationPicturesActivity.class, bundle, false);
        });
    }

    @Override
    public int getItemCount() {
        return venueListMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lyt_root) LinearLayout lyt_root;
        @BindView(R.id.tv_place_title) TextView tv_place_title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
        }
    }
}
