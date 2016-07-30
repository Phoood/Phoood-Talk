package com.phooodstudio.phooodtalk.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.phooodstudio.phooodtalk.R;
import com.phooodstudio.phooodtalk.model.Message;

/**
 * This activity is responsible for everything related to chat.
 */
public class ChatActivity extends AppCompatActivity {

    /*
     * request code for startActivityForResult method.
     * It is an arbitrary integer.
     */
    private static final int REQUEST_CODE_CAMERA = 100;

    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
            Message msg = new Message();
            msg.setContents(string);
            mAdapter.add(msg);
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
                msg.setContents(photo);
                mAdapter.add(msg);
            }
        }
    }
}
