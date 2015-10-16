package com.android.photoapp.ui;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.photoapp.R;
import com.android.photoapp.data.PhotoAppContract;
import com.android.photoapp.data.PhotoAppDatabaseHelper;
import com.android.photoapp.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import java.io.File;

public class SetPhotoActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageView mPhoto;
    private EditText mCaption;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitude;
    private String mLongitude;
    protected static final String TAG = "SetPhotoActivity";
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_photo);

        mPhoto = (ImageView) findViewById(R.id.photo);
        mCaption = (EditText) findViewById(R.id.caption);

        setBitmap();
        buildGoogleApiClient();
        findViewById(R.id.okBtn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);

        if (!Util.getGpsStatus(this)) {
            Util.showSettingsAlert(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okBtn) {
            saveData();
        } else if (v.getId() == R.id.cancelBtn) {
            finish();
        }
    }

    private void setBitmap() {
        path = getIntent().getStringExtra("photo_path");
        path = Util.compressImage(path);
        Bitmap bitmap = Util.getBitmapFromPath(path);
        if (bitmap != null) {
            mPhoto.setImageBitmap(bitmap);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitude = String.valueOf(Util.formatFigureToTwoPlaces(mLastLocation.getLatitude()));
            mLongitude = String.valueOf(Util.formatFigureToTwoPlaces(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    private void saveData() {
        if(!TextUtils.isEmpty(mCaption.getText().toString())) {
            ContentValues values = new ContentValues();
            values.put(PhotoAppContract.PhotoEntry.COLUMN_NAME_IMAGE_CAPTION, mCaption.getText().toString());
            values.put(PhotoAppContract.PhotoEntry.COLUMN_NAME_IMAGE_URL, path);
            values.put(PhotoAppContract.PhotoEntry.COLUMN_NAME_LATITUDE, mLatitude);
            values.put(PhotoAppContract.PhotoEntry.COLUMN_NAME_LONGITUDE, mLongitude);

            PhotoAppDatabaseHelper dbHelper = PhotoAppDatabaseHelper.getInstance(this);
            dbHelper.insert(values);
            finish();
        } else }
            mCaption.setError(getString(R.string.caption_empty_err));
        }
    }
}
