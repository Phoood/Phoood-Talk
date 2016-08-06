package com.phooodstudio.phooodtalk.presentation;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phooodstudio.phooodtalk.PhooodTalkApp;
import com.phooodstudio.phooodtalk.R;
import com.phooodstudio.phooodtalk.model.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Login Activity
 * This is the first activity that will run
 */
public class LoginActivity extends AppCompatActivity {

    private PhooodTalkApp mApplication;
    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;
    private Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private static List<String> FACEBOOK_PERMISSIONS =
            new ArrayList<>();

    private static final int PERMISSION_REQUEST_READ_EXT_STORAGE = 1;
    private static final int PERMISSION_REQUEST_INTERNET = 2;
    private static final int PERMISSION_REQUEST_WRITE_EXT_STORAGE = 3;
    private static final String TAG = "LoginActivity";

    public LoginActivity() {
        super();

        FACEBOOK_PERMISSIONS.add("email");
        FACEBOOK_PERMISSIONS.add("public_profile");
        FACEBOOK_PERMISSIONS.add("user_birthday");
        FACEBOOK_PERMISSIONS.add("user_friends");
        FACEBOOK_PERMISSIONS.add("user_about_me");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApplication = (PhooodTalkApp) getApplication();

        //UI Elements
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mLoginButton.setReadPermissions(FACEBOOK_PERMISSIONS);
        // Callback registration
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            /**
             * On success, go to the home screen
             * @param loginResult - result of the login
             */
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookCallbackOnSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                facebookCallbackOnCancel();
            }

            @Override
            public void onError(FacebookException exception) {
                facebookCallbackOnError(exception);
            }
        });
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookCallbackOnSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                facebookCallbackOnCancel();
            }

            @Override
            public void onError(FacebookException error) {
                facebookCallbackOnError(error);
            }
        });

        //Authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        //Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener);

        if (getIntent().hasExtra("logged out")){ //do nothing
            Log.d(TAG, "Logged out");
        }
        else{
            //Run login
            LoginManager.getInstance().logInWithReadPermissions(this, FACEBOOK_PERMISSIONS);
            Log.d(TAG, "Logging in");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFirebaseAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener);
        }
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

    /**
     * Processes results from activity
     * For LoginActivity, this is just processing the result of Facebook login
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * This method is responsible for linking the Firebase account with the Facebook Account
     *
     * @param token - grants access to the linked Firebase account
     */
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        //Receive and sign in with credential from facebook.
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void handleFirebaseEntry(JSONObject jsonObject) {
        String facebookId = "1";
        String name = "default user";
        String email = "email@example.com";
        String birthday = "01/01/1901";
        String gender = "n/a";

        // Application code
        try {
            facebookId = jsonObject.getString("id");
            name = jsonObject.getString("name");
            email = jsonObject.getString("email");
            birthday = jsonObject.getString("birthday"); // 01/31/1980 format
            gender = jsonObject.getString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Retrieve account root
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");
        DatabaseReference accountRoot = reference.child(facebookId);

        //Set fields
        accountRoot.child("name").setValue(name);
        accountRoot.child("email").setValue(email);
        accountRoot.child("birthday").setValue(birthday);
        accountRoot.child("gender").setValue(gender);

        //Set last login time
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long time = cal.getTimeInMillis();
        accountRoot.child("last login").setValue(time);

        //Create static account object
        Account currentAccount =  new Account();
        currentAccount.setName(name);
        currentAccount.setId(facebookId);
        currentAccount.setEmail(email);
        currentAccount.setBirthday(birthday);
        currentAccount.setGender(gender);
        currentAccount.setLastLogin(time);
        mApplication.setCurrentAccount(currentAccount);
    }


    /**
     * Helper method for success callback
     *
     * @param loginResult
     */
    private void facebookCallbackOnSuccess(LoginResult loginResult) {
        Log.i(TAG, "Connected to Facebook");
        handleFacebookAccessToken(loginResult.getAccessToken());

        //Requesting email and putting it into database
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());
                        handleFirebaseEntry(object);
                    }
                });

        //Create bundle to specify data
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();

        Intent homeIntent = new Intent(mContext, HomeActivity.class);
        startActivity(homeIntent);
    }

    private void facebookCallbackOnCancel() {
        Log.i(TAG, "Canceled connection to Facebook");

    }

    private void facebookCallbackOnError(FacebookException exception) {
        Log.e(TAG, "Error connecting to Facebook ");

    }

}
