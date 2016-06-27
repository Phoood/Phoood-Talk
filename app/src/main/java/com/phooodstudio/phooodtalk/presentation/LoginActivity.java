package com.phooodstudio.phooodtalk.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.phooodstudio.phooodtalk.R;

/**
 * Login Activity
 * This is the first activity that will run
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * This is a temporary login method.
     * Only use for debugging.
     * @param view - view that activated this
     */
    @Deprecated
    public final void tempLogin(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
