package com.smartalgorithms.locationpictures.Models;

import com.google.gson.Gson;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class LocationResponse extends NetworkResponse{
    private List<Result> results;
    private GeneralHelper generalHelper;

    @Inject
    public LocationResponse(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public static String getAdress(Result result) {
        return result.getFormatted_address();
    }

    public LocationResponse fromJson(String s) {
        return (LocationResponse) generalHelper.getObjectFromJson(s, LocationResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public List<Result> getResults() {
        return results;
    }

    public class Result {
        private String formatted_address;
        private List<Component> address_components;

        public String getFormatted_address() {
            return formatted_address;
        }

        public List<Component> getAddress_components() {
            return address_components;
        }

        class Component {
            public String long_name;
            public String short_name;
            public List<String> types;
        }
    }
}
