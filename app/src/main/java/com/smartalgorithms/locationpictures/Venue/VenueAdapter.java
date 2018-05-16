package com.smartalgorithms.locationpictures.Venue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartalgorithms.locationpictures.CustomViews.ZoomableImageView;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;
import com.smartalgorithms.locationpictures.Models.VenueResponse;
import com.smartalgorithms.locationpictures.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/16.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/16.
 */

public class VenueAdapter extends PagerAdapter{

    private List<VenueResponse.Response.Venue.Photo.Group.Item> photos;
    private GeneralHelper generalHelper;

    public VenueAdapter(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public void setPhotos(List<VenueResponse.Response.Venue.Photo.Group.Item> photos) {
        this.photos = photos;
    }

    @BindView(R.id.ziv_image_display) ZoomableImageView ziv_image_display;
    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = layoutInflater.inflate(R.layout.item_venue_photo, container, false);
        ButterKnife.bind(VenueAdapter.this, viewLayout);
        String imageUrl = photos.get(position).getPrefix() + photos.get(position).getWidth() + "x" + photos.get(position).getHeight() + photos.get(position).getSuffix();
        generalHelper.imageLoaderUrl(context, ziv_image_display, imageUrl, false);
        container.addView(viewLayout);
        return viewLayout;
    }
}
