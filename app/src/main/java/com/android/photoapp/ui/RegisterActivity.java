package com.android.photoapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.android.photoapp.MainActivity;
import com.android.photoapp.R;
import com.android.photoapp.data.SavePreferences;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mPersonName;
    private EditText mPhoneNumber;
    private SavePreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPersonName = (EditText) findViewById(R.id.content_reg_name);
        mPhoneNumber = (EditText) findViewById(R.id.content_reg_phone);

        mPrefs = new SavePreferences(this);
        checkIfAlreadyRegistered();

        findViewById(R.id.next).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next) {
            if (validate()) {
                saveData();
            }
        }
    }

    private void checkIfAlreadyRegistered() {
        if(mPrefs.isLoggedIn())
            startMainActivity();
    }

    private void saveData() {
        mPrefs.setUserName(mPersonName.getText().toString());
        mPrefs.setUserPhoneNumber(mPhoneNumber.getText().toString());
        mPrefs.setIsLoggedIn(true);
        startMainActivity();
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean validate() {
        boolean flag = true;
        String name = mPersonName.getText().toString();
        String phone = mPhoneNumber.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mPersonName.setError(getString(R.string.name_validation_error));
            flag = false;
        } else if (TextUtils.isEmpty(phone)) {
            mPhoneNumber.setError(getString(R.string.phone_empty_err));
            flag = false;
        } else if (phone.length() < 10) {
            mPhoneNumber.setError(getString(R.string.phone_length_error));
            flag = false;
        }

        return flag;
    }
}
