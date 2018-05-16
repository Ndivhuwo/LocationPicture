package com.smartalgorithms.locationpictures.Models;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ndivhuwo Nthambeleni on 2018/05/15.
 * Updated by Ndivhuwo Nthambeleni on 2018/05/15.
 */

public class VenueResponse extends NetworkResponse{
    private Response response;

    @Inject
    public VenueResponse() {
    }

    public Response getResponse() {
        return response;
    }

    public class Response {
        private Venue venue;

        public Venue getVenue() {
            return venue;
        }

        public class Venue {
            private Tip tips;
            private Photo photos;
            private String name;
            private NearByPlacesResponse.Response.Group.Item.Venue.Loc location;

            public String getName() {
                return name;
            }

            public NearByPlacesResponse.Response.Group.Item.Venue.Loc getLocation() {
                return location;
            }

            public Tip getTips() {
                return tips;
            }

            public Photo getPhotos() {
                return photos;
            }

            public class Photo {
                private List<Group> groups;

                public List<Group> getGroups() {
                    return groups;
                }

                public class Group {
                    private List<Item> items;

                    public List<Item> getItems() {
                        return items;
                    }

                    public class Item {
                         private long createdAt;
                         private String prefix;
                         private String suffix;
                         private int width;
                         private int height;

                        public int getWidth() {
                            return width;
                        }

                        public int getHeight() {
                            return height;
                        }

                        public long getCreatedAt() {
                            return createdAt;
                        }

                        public String getPrefix() {
                            return prefix;
                        }

                        public String getSuffix() {
                            return suffix;
                        }
                    }
                }
            }

            public class Tip {
                private List<Group> groups;

                public List<Group> getGroups() {
                    return groups;
                }

                public class Group {
                    private List<Item> items;

                    public List<Item> getItems() {
                        return items;
                    }

                    public class Item {
                        private long createdAt;
                        private String photourl;

                        public long getCreatedAt() {
                            return createdAt;
                        }

                        public String getPhotourl() {
                            return photourl;
                        }
                    }
                }
            }
        }
    }
}
