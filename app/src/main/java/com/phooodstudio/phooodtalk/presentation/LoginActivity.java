package com.phooodstudio.phooodtalk.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.phooodstudio.phooodtalk.R;

/**
 * Login Activity
 * This is the first activity that will run
 */
public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_READ_EXT_STORAGE = 1;
    private static final int PERMISSION_REQUEST_INTERNET = 2;
    private static final int PERMISSION_REQUEST_WRITE_EXT_STORAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXT_STORAGE);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_INTERNET);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXT_STORAGE);
        }
    }

    /**
     * This is a temporary login method.
     * Only use for debugging.
     * @param view - view that activated this
     */
    @Deprecated
    public final void tempLogin(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * When permissions are granted, run the following lines of code
     * TODO: create dialogs when certain permissions are not granted.
     *
     * @param requestCode  - permission request
     * @param permissions  - permissions that were granted
     * @param grantResults - result of each grant
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXT_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.err.println("Permission: Read External Storage Granted");
                } else {
                    System.err.println("Permission: Read External Storage Not Granted");
                }
            }

            case PERMISSION_REQUEST_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.err.println("Permission: Internet Granted");
                } else {
                    System.err.println("Permission: Internet Not Granted");
                }
            }

            case PERMISSION_REQUEST_WRITE_EXT_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.err.println("Permission: Write External Storage Granted");
                } else {
                    System.err.println("Permission: Write External Storage Not Granted");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
