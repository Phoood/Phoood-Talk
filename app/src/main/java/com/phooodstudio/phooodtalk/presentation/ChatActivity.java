package com.phooodstudio.phooodtalk.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.phooodstudio.phooodtalk.PhooodTalkApp;
import com.phooodstudio.phooodtalk.R;
import com.phooodstudio.phooodtalk.model.Account;
import com.phooodstudio.phooodtalk.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * This activity is responsible for everything related to chat.
 */
public class ChatActivity extends AppCompatActivity {

    /*
     * request code for startActivityForResult method.
     * It is an arbitrary integer.
     */
    private static final int REQUEST_CODE_CAMERA = 100;
    public static final String TAG = "ChatActivity";

    private PhooodTalkApp mApplication;
    private Account mOtherPerson;
    private MessageAdapter mAdapter;

    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mApplication = (PhooodTalkApp) getApplication();

        //Get the other account
        Intent receivedIntent = getIntent();
        mOtherPerson = (Account) receivedIntent.getSerializableExtra(FriendsFragment.ACCOUNT_EXTRA);
        if (mOtherPerson == null) {
            Log.e(TAG, "Received account is null");
        }

        //Create chat ID
        final String currentId = mApplication.getCurrentAccount().getId();
        String otherId = mOtherPerson.getId();
        long currentIdLong = Long.parseLong(currentId);
        long otherIdLong = Long.parseLong(otherId);
        if (currentIdLong < otherIdLong) {
            chatId = currentId + " " + otherId;
        } else if (otherIdLong < currentIdLong) {
            chatId = otherId + " " + currentId;
        } else {
            Log.wtf(TAG, "You seem to be talking to yourself.");
        }
        Log.d(TAG, "chatId: " + chatId);


        //Set adapter
        mAdapter = new MessageAdapter(this, mApplication.getCurrentAccount().getId());
        ListView listView = (ListView) findViewById(R.id.chat_messages);
        if (listView != null) {
            listView.setAdapter(mAdapter);
        }
    }

    /**
     * Gets the text from view and send it to the server (well, supposedly)
     *
     * @param view the view that activated this method
     */
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.chat);
        if (editText != null) {
            Editable text = editText.getText();
            String string = text.toString();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            long time = cal.getTimeInMillis();

            Message msg = new Message();
            msg.setContents(string);
            msg.setSender(mApplication.getCurrentAccount().getId());
            msg.setTimeSent(Long.toString(time));
            msg.setType(Message.TYPE_STRING);

            //send message to firebase

            text.clear();
        }
    }

    /**
     * Initiates taking picture from Photo app in the phone.
     *
     * @param view the view that activated this method
     */
    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * Checks for the camera intent result.
     *
     * @param requestCode the request code passed with startActivityForResult method
     * @param resultCode  the result
     * @param data        the data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Message msg = new Message();
                //msg.setContents(photo);
                mAdapter.add(msg);
            }
        }
    }


}
