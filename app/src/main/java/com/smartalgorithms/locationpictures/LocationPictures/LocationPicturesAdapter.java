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
    private VenueResponse venueResponse;
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

    public void setVenueResponseList(VenueResponse venueResponse) {
        this.venueResponse = venueResponse;//sortList(venueResponseList);
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

        VenueResponse.Response.Venue venue = venueResponse.getResponse().getVenue();

        holder.rlyt_root.setOnClickListener(view -> {
            System.out.println(position);
            bundle.putString(Constants.INTENT_EXTRA_PLACE, generalHelper.getJSONFromObject(venueResponse.getResponse().getVenue()));
            listener.onGridItemClick(bundle);
        });
        if(venue.getPhotos() != null && venue.getPhotos().getGroups().size() > 0 && venue.getPhotos().getGroups().get(0).getItems().size() > 0) {
            String imageUrl = venue.getPhotos().getGroups().get(0).getItems().get(position).getPrefix() +
                    venue.getPhotos().getGroups().get(0).getItems().get(position).getWidth() + "x" +
                    venue.getPhotos().getGroups().get(0).getItems().get(position).getHeight() +
                    venue.getPhotos().getGroups().get(0).getItems().get(position).getSuffix();
            generalHelper.imageLoaderUrl(context, holder.iv_image, imageUrl, true);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (venueResponse.getResponse().getVenue().getPhotos().getGroups().get(0).getItems() != null)
            return venueResponse.getResponse().getVenue().getPhotos().getGroups().get(0).getItems().size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlyt_root) RelativeLayout rlyt_root;
        @BindView(R.id.iv_image) ImageView iv_image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(ViewHolder.this, view);
        }
    }
}
