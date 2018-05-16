package com.smartalgorithms.locationpictures.Network;

import android.support.annotation.Nullable;

import com.smartalgorithms.locationpictures.Helpers.LoggingHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class NetworkUseCase {
    private static final String TAG = NetworkUseCase.class.getSimpleName();
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";
    private LoggingHelper loggingHelper;

    @Inject
    public NetworkUseCase(LoggingHelper loggingHelper) {
        this.loggingHelper = loggingHelper;
    }

    public Response networkGET(HttpUrl url){
        loggingHelper.d(TAG, "networkGet");
        final Request request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();

        return getResponse(request);
    }

    public Response networkPOST(HttpUrl url, @Nullable RequestBody requestBody){
        loggingHelper.d(TAG, "networkGet");
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON);
        if(requestBody != null)
            requestBuilder.post(requestBody);

        return getResponse(requestBuilder.build());
    }

    private Response getResponse(Request request){
        loggingHelper.d(TAG, "getResponse");

        Response response;
        MediaType JSON=MediaType.parse(CONTENT_TYPE_JSON);
        try {
            response = getClient().newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            response = new Response.Builder()
                    .code(503)
                    .body(ResponseBody.create(JSON, e.getMessage()))
                    .build();
        }

        return response;
    }

    private OkHttpClient getClient() {
        loggingHelper.d(TAG, "getClient");
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }
}
