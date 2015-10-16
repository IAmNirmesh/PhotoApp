package com.android.photoapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.android.photoapp.data.SavePreferences;
import com.android.photoapp.ui.PhotoGalleryActivity;
import com.android.photoapp.ui.SetPhotoActivity;
import com.android.photoapp.ui.SettingsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CAMERA_REQUEST = 1005;
    private Uri mCapturedImageURI;
    private String IMAGE_PATH;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (TextView) findViewById(R.id.userName);
        findViewById(R.id.takePicture).setOnClickListener(this);
        findViewById(R.id.explorePhoto).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = "Hi " + new SavePreferences(this).getUserName();
        userName.setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mCapturedImageURI = data.getData();
        } else if (requestCode == CAMERA_REQUEST
                && resultCode == Activity.RESULT_OK) {
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(mCapturedImageURI,
                    projection, null, null, null);
            assert cursor != null;
            int column_index_data = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            IMAGE_PATH = cursor.getString(column_index_data);
            openSetPhotoActivity();
            cursor.close();
        }
    }

    private void openSetPhotoActivity() {
        Intent intent = new Intent(this, SetPhotoActivity.class);
        intent.putExtra("photo_path", IMAGE_PATH);
        startActivity(intent);
    }

    private void openCamera() {
        String fileName = String.valueOf(System.currentTimeMillis());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        mCapturedImageURI = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        intent.putExtra("return-data", false);

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.takePicture) {
            openCamera();
        } else if(v.getId() == R.id.explorePhoto) {
            startActivity(new Intent(this, PhotoGalleryActivity.class));
        }
    }
}
