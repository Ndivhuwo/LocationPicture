package com.smartalgorithms.locationpictures.Models;

import com.google.gson.Gson;
import com.smartalgorithms.locationpictures.Helpers.GeneralHelper;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/14.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/14.
 */

public class NearByPlacesResponse extends NetworkResponse{
    private Response response;

    private GeneralHelper generalHelper;

    @Inject
    public NearByPlacesResponse(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public NearByPlacesResponse fromJson(String s) {
        return (NearByPlacesResponse) generalHelper.getObjectFromJson(s, NearByPlacesResponse.class);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public Response getResponse() {
        return response;
    }

    public class Response {

        private List<Group> groups;

        public List<Group> getGroups() {
            return groups;
        }

        public class Group {
            private String type;
            private String name;
            private List<Item> items;

            public String getType() {
                return type;
            }

            public String getName() {
                return name;
            }

            public List<Item> getItems() {
                return items;
            }

            public class Item {
                private Venue venue;

                public Venue getVenue() {
                    return venue;
                }

                public class Venue {
                    private String id;
                    private String name;
                    private Loc location;

                    public String getName() {
                        return name;
                    }

                    public Loc getLocation() {
                        return location;
                    }

                    public String getId() {
                        return id;
                    }

                    public class Loc {
                        private String address;
                        private String crossStreet;
                        private double lat;
                        private double lng;
                        private int distance;
                        private int postalCode;
                        private String[] formattedAddress;

                        public void setDistance(int distance) {
                            this.distance = distance;
                        }

                        public String[] getFormattedAddress() {
                            return formattedAddress;
                        }

                        public int getPostalCode() {
                            return postalCode;
                        }

                        public String getAddress() {
                            return address;
                        }

                        public String getCrossStreet() {
                            return crossStreet;
                        }

                        public double getLat() {
                            return lat;
                        }

                        public double getLng() {
                            return lng;
                        }

                        public int getDistance() {
                            return distance;
                        }
                    }

                }
            }
        }
    }
}
