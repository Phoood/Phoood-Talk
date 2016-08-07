package com.phooodstudio.phooodtalk.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.phooodstudio.phooodtalk.PhooodTalkApp;
import com.phooodstudio.phooodtalk.R;
import com.phooodstudio.phooodtalk.database.FacebookHelper;
import com.phooodstudio.phooodtalk.model.Account;

public class FriendActivity extends AppCompatActivity {

    public static final String TAG = "FriendActivity";

    private Account mFriend;
    private PhooodTalkApp mApplication;

    //Views
    private ImageView mProfilePicture;
    private TextView mName;
    private TextView mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        mApplication = (PhooodTalkApp) getApplication();

        //Receive other account
        Intent receivedIntent = getIntent();
        mFriend = (Account) receivedIntent.getSerializableExtra(FriendsFragment.ACCOUNT_EXTRA);
        if (mFriend == null) {
            Log.e(TAG, "Received account is null");
        }
        assert mFriend != null;

        //Assign views
        mProfilePicture = (ImageView) findViewById(R.id.activity_friend_profile_picture);
        mName = (TextView) findViewById(R.id.activity_friend_user_name);
        if (mName != null){
            mName.setText(mFriend.getName());
        }
        mNickname = (TextView) findViewById(R.id.activity_friend_nickname);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Start getting profile picture
        FacebookHelper.RetrieveImageTask retriever =
                new FacebookHelper.RetrieveImageTask(mProfilePicture);
        retriever.execute(mFriend);
    }
}
