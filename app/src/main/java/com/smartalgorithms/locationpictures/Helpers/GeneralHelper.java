package com.smartalgorithms.locationpictures.Helpers;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.smartalgorithms.locationpictures.App;
import com.smartalgorithms.locationpictures.R;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class GeneralHelper {
    private Context context;
    private Gson gson;

    @Inject
    public GeneralHelper(Context context, Gson gson) {
        this.context = context;
        this.gson = gson;
    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

        //Check GPS provider
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check network provider
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gps_enabled || network_enabled;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public Object getObjectFromJson(String json, Class<?> classType) {
        return gson.fromJson(json, classType);
    }

    public String getJSONFromObject(Object object) {
        return gson.toJson(object);
    }

    public void imageLoaderUrl(Context context, ImageView imageView, String url, boolean placeHolder){
        if(placeHolder) {
            RequestOptions requestOptions = new RequestOptions()
                    .centerInside()
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.image_error);
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
        }else {
            Glide.with(context)
                    .load(url)
                    .into(imageView);
        }

    }

    public static class IntegerTypeAdapter extends TypeAdapter<Integer> {
        @Override
        public void write(JsonWriter jsonWriter, Integer number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number);
        }

        @Override
        public Integer read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            int value;
            try {
                //String value = jsonReader.nextString();
                value = jsonReader.nextInt();
            } catch (NumberFormatException e) {
                //throw new JsonSyntaxException(e);
                value = 0;
            }
            return  value;
        }

    }
}
