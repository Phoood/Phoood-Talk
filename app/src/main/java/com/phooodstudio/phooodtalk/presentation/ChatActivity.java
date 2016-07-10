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

public class ChatActivity extends AppCompatActivity {

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

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

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
