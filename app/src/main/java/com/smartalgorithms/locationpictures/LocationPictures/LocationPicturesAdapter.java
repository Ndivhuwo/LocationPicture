package com.smartalgorithms.locationpictures.LocationPictures;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartalgorithms.locationpictures.Constants;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Models.VenueResponse;
import com.smartalgorithms.locationpictures.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class LocationPicturesAdapter extends RecyclerView.Adapter<LocationPicturesAdapter.ViewHolder>{
    private static final String TAG = LocationPicturesAdapter.class.getSimpleName();
    private List<VenueResponse> venueResponseList;
    private Context context;
    private Bundle bundle;
    private LocationPicturesContract.AdapterPresenterListener listener;
    private GeneralHelper generalHelper;

    @Inject
    public LocationPicturesAdapter(Bundle bundle, LocationPicturesContract.AdapterPresenterListener listener, GeneralHelper generalHelper) {
        this.bundle = bundle;
        this.listener = listener;
        this.generalHelper = generalHelper;
    }

    public void setVenueResponseList(List<VenueResponse> venueResponseList) {
        this.venueResponseList = sortList(venueResponseList);
    }

    private List<VenueResponse> sortList(List<VenueResponse> venueResponseList) {
        List<VenueResponse> sortedList = new ArrayList<>();
        sortedList.addAll(venueResponseList);
        Collections.sort(sortedList, (VenueResponse left, VenueResponse right) -> {
            int rightDist = right.getResponse().getVenue().getLocation().getDistance();
            int leftDist = left.getResponse().getVenue().getLocation().getDistance();
            return (rightDist>leftDist ? -1 : (rightDist == leftDist ? 0 : 1));
        });
        return sortedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VenueResponse.Response.Venue venue = venueResponseList.get(position).getResponse().getVenue();

        holder.rlyt_root.setOnClickListener(view -> {
            System.out.println(position);
            bundle.putString(Constants.INTENT_EXTRA_PLACE, generalHelper.getJSONFromObject(venueResponseList.get(position).getResponse().getVenue()));
            listener.onGridItemClick(bundle);
        });
        if(venue.getPhotos() != null && venue.getPhotos().getGroups().size() > 0 && venue.getPhotos().getGroups().get(0).getItems().size() > 0) {
            String imageUrl = venue.getPhotos().getGroups().get(0).getItems().get(0).getPrefix() +
                    venue.getPhotos().getGroups().get(0).getItems().get(0).getWidth() + "x" +
                    venue.getPhotos().getGroups().get(0).getItems().get(0).getHeight() +
                    venue.getPhotos().getGroups().get(0).getItems().get(0).getSuffix();
            generalHelper.imageLoaderUrl(context, holder.iv_image, imageUrl, true);
        }
        String title = venue.getName().length() > 35 ? venue.getName().substring(0, 35) + "..." : venue.getName();
        holder.tv_title.setText(title);
        String distance = (double)venue.getLocation().getDistance()/1000 + " KM Away";
        holder.tv_distance.setText(distance);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (venueResponseList != null)
            return venueResponseList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlyt_root) RelativeLayout rlyt_root;
        @BindView(R.id.iv_image) ImageView iv_image;
        @BindView(R.id.llyt_details) LinearLayout llyt_details;
        @BindView(R.id.tv_title) TextView tv_title;
        @BindView(R.id.tv_distance) TextView tv_distance;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(ViewHolder.this, view);
        }
    }
}
