package com.android.photoapp.data;

import android.provider.BaseColumns;

public class PhotoAppContract {
    private PhotoAppContract() {

    }

    /* Inner class that defines the table contents */
    public static abstract class PhotoEntry implements BaseColumns {
        public static final String TABLE_NAME = "photos";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
        public static final String COLUMN_NAME_IMAGE_CAPTION = "image_caption";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }
}
