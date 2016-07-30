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
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
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

        //Initialize databases
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("messages").child(chatId);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "Received new Data" + dataSnapshot.toString());
                Message message = dataSnapshot.getValue(Message.class);

//                JSONObject jsonObject = (JSONObject) dataSnapshot.getValue(JSONObject.class);
//
//                try {
//                    message.setContents(jsonObject.getString("message"));
//                    message.setSender(jsonObject.getString("sender"));
//                    message.setTimeSent(Long.parseLong(jsonObject.getString("time")));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                mAdapter.add(message);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                //should not happen
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //should not happen
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                //should not happen
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Data received canceled");

            }
        });

        //Initialize Storage
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReferenceFromUrl("gs://phooodtalk.appspot.com");

        mAdapter = new MessageAdapter(this);

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

            sendMessageToFirebase(msg);

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


    /**
     * Sends the message to Firebase.
     * Uses the time sent as an ID
     *
     * @param message
     */
    private void sendMessageToFirebase(Message message) {
        DatabaseReference messageRootRef = mDatabaseReference.child(message.getTimeSent() + "");
//        JSONObject messageObject = new JSONObject();
//        try {
//            messageObject.put("message", (String) message.getContents());//TODO: change implementation
//            messageObject.put("sender", message.getSender());
//            messageObject.put("time", message.getTimeSent() + "");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        messageRootRef.setValue(message);
    }

}
