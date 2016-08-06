package com.phooodstudio.phooodtalk.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
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
import com.phooodstudio.phooodtalk.database.FirebaseHelper;
import com.phooodstudio.phooodtalk.model.Account;
import com.phooodstudio.phooodtalk.model.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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

    private String photoFilename;
    private Uri photoUri;

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

        //Set data listener for adapter
        //Note: this currently will get everything
        FirebaseHelper.getInstance().getDatabaseReference("messages", chatId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "Received new Data" + dataSnapshot.toString());
                        Message message = dataSnapshot.getValue(Message.class);
                        mAdapter.add(message);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Chat " + chatId + " onCanceled called");
                    }
                });

        //Change name of support action bar
        getSupportActionBar().setTitle(mOtherPerson.getName());
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

            //Create new message
            Message msg = new Message();
            msg.setContents(string);
            msg.setSender(mApplication.getCurrentAccount().getId());
            msg.setTimeSent(Long.toString(time));
            msg.setType(Message.TYPE_STRING);

            //send message to firebase
            FirebaseHelper.getInstance().sendMessage(msg, chatId);

            //Clear the text in the chat window
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

        File photoFile = null;

        long time = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();

        // create temporary file for saving image
        try {
            photoFile = File.createTempFile(
                mApplication.getCurrentAccount().getId() + "_" + time,
                ".jpg",
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (photoFile != null) {

            photoFilename = photoFile.getName();

            // get URI for the file
            photoUri = FileProvider.getUriForFile(
                this,
                "com.phooodstudio.phooodtalk.fileprovider",
                photoFile
            );

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            // check if any app can handle taking picture
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        }
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
        switch (requestCode) {

            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                        FirebaseHelper.getInstance().sendImage(bitmap, 0, "images", photoFilename);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}