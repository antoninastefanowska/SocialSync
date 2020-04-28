package com.antonina.socialsynchro.services.facebook.content;

import android.webkit.URLUtil;

import com.antonina.socialsynchro.common.content.posts.PostOptions;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacebookPostOptions extends PostOptions {
    public static final String[] CALL_TO_ACTION_TYPES = {
            "BOOK_TRAVEL",
            "BUY NOW",
            "CALL_NOW",
            "DOWNLOAD",
            "GET_DIRECTIONS",
            "GET_QUOTE",
            "INSTALL_APP",
            "INSTALL_MOBILE_APP",
            "LEARN_MORE",
            "LIKE_PAGE",
            "LISTEN_MUSIC",
            "MESSAGE_PAGE",
            "NO_BUTTON",
            "OPEN_LINK",
            "PLAY_GAME",
            "SHOP_NOW",
            "SIGN_UP",
            "SUBSCRIBE",
            "USE_APP",
            "USE_MOBILE_APP",
            "WATCH_MORE",
            "WATCH_VIDEO"
    };
    public static final Map<String, Integer> RELATIONSHIP_STATUSES_VALUES = new HashMap<String, Integer>() {{
        put("single", 1);
        put("in a relationship", 2);
        put("married", 3);
        put("engaged", 4);
    }};

    private Link link;
    private String callToActionType;
    private FeedTargeting feedTargeting;
    private GeoLocations geoLocations;
    private List<Interest> interests;
    private List<Integer> relationshipStatuses;

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {

    }

    @Override
    public void saveInDatabase() {

    }

    @Override
    public void updateInDatabase() {

    }

    @Override
    public void deleteFromDatabase() {

    }

    public static class Link {
        private String url;
        private String description;
        private String name;
        private File thumbnail;

        public boolean isValid() {
            return URLUtil.isValidUrl(url);
        }
    }

    public static class FeedTargeting {
        public static final int[] COLLEGE_YEARS_VALUES = {1, 2, 3, 4, 5, 6};
        public static final Map<String, Integer> EDUCATION_STATUSES_VALUES = new HashMap<String, Integer>() {{
            put("high school", 1);
            put("undergraduate", 2);
            put("alum", 3);
        }};
        public static final int[] GENDERS_VALUES = {1, 2};

        private int ageMin;
        private int ageMax;
        private List<Integer> collegeYears;
        private List<Integer> educationStatuses;
        private List<Integer> genders;
    }

    public static class GeoLocations {
        private List<String> countries;
    }

    public static class Interest {
        private String name;
        private int key;
    }
}
