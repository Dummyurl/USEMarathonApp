package ru.use.marathon.models.data;

import android.provider.BaseColumns;

public final class HotelContract {
//база данных пример
    // понял, поменяешь название потом
    private HotelContract() {
    }

    public static final class GuestEntry implements BaseColumns {
        public final static String TABLE_NAME = "guests";

        public final static String COLUMN_TITLE = "Title";
        public final static String COLUMN_CONTENT = "Content";
        public final static String COLUMN_IMAGE = "Image";
        public final static String COLUMN_TEXTCONTENT = "Textcontent";
        public final static String COLUMN_CREATED_AT = "Created_at";
        //  public final static String COLUMN_TEXT = "Text";
        //        //public final static String _ID = BaseColumns._ID;
    }
}
