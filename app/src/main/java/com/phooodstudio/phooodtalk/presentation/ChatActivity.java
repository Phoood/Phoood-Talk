package com.phooodstudio.phooodtalk.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.phooodstudio.phooodtalk.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        adp = new ArrayAdapter<String>(this, R.layout.chat_message);

        ListView listView = (ListView) findViewById(R.id.chat_messages);
        listView.setAdapter(adp);
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.chat);
        Editable text = editText.getText();
        String string = text.toString();
        adp.add(string);
        text.clear();
    }
}
