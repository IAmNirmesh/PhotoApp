package com.android.photoapp.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import com.android.photoapp.R;
import com.android.photoapp.adapter.GalleryAdapter;
import com.android.photoapp.data.PhotoAppContract;
import com.android.photoapp.data.PhotoAppDatabaseHelper;

public class PhotoGalleryActivity extends AppCompatActivity {

    private GridView mGridView;
    private TextView mEmptyText;
    private GalleryAdapter mAdapter;

    public static final int COL_PHOTO_ID = 0;
    public static final int COL__PHOTO_URL = 1;
    public static final int COL_PHOTO_CAPTION = 2;
    public static final int COL_PHOTO_LATITUDE = 3;
    public static final int COL_PHOTO_LONGITUDE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new GalleryAdapter(this, null, 0);
        mGridView = (GridView) findViewById(R.id.photoView);
        mEmptyText = (TextView) findViewById(R.id.emptyText);
        mGridView.setAdapter(mAdapter);

        //Get photos saved in sql database
        new GetPhotosAsync().execute();
    }

    private class GetPhotosAsync extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... strings) {
            Cursor cursor = PhotoAppDatabaseHelper.getInstance(PhotoGalleryActivity.this).getData();
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (!cursor.moveToFirst()) {
                mEmptyText.setVisibility(View.VISIBLE);
            } else {
                mEmptyText.setVisibility(View.GONE);
            }

            mAdapter = new GalleryAdapter(PhotoGalleryActivity.this, cursor, 0);
            mGridView.setAdapter(mAdapter);
        }
    }
}
