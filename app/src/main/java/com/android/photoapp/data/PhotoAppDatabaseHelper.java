package com.android.photoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhotoAppDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Photo.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PhotoAppContract.PhotoEntry.TABLE_NAME + " (" +
                    PhotoAppContract.PhotoEntry._ID + " INTEGER PRIMARY KEY," +
                    PhotoAppContract.PhotoEntry.COLUMN_NAME_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
                    PhotoAppContract.PhotoEntry.COLUMN_NAME_IMAGE_CAPTION + TEXT_TYPE + COMMA_SEP +
                    PhotoAppContract.PhotoEntry.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP +
                    PhotoAppContract.PhotoEntry.COLUMN_NAME_LONGITUDE + TEXT_TYPE +

            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PhotoAppContract.PhotoEntry.TABLE_NAME;

    private static final String SQL_GET_PHOTOS = "SELECT *FROM " + PhotoAppContract.PhotoEntry.TABLE_NAME;

    private static PhotoAppDatabaseHelper dbHelper = null;
    public static PhotoAppDatabaseHelper getInstance(Context context){
        if(dbHelper == null) {
            dbHelper = new PhotoAppDatabaseHelper(context);
            return dbHelper;
        } else {
            return dbHelper;
        }
    }

    private PhotoAppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(PhotoAppContract.PhotoEntry.TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(SQL_GET_PHOTOS, null);
        return cursor;
    }
}
