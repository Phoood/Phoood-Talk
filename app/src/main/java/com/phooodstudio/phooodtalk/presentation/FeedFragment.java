package com.phooodstudio.phooodtalk.presentation;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phooodstudio.phooodtalk.R;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 * Defines the fragment to show the friends list
 */
public class FeedFragment extends Fragment {

    private static final String TAG = "FeedFragment";

    private MultiAutoCompleteTextView mEditText;
    private TextView mTextView;
    private Button mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_feed, container, false);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("message");

                mEditText = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_feed_edit_text);
                mTextView = (TextView) view.findViewById(R.id.fragment_feed_text_view);
                mButton = (Button) view.findViewById(R.id.enter_button);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRef.setValue(mEditText.getText().toString());
                    }
                });

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                mTextView.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return view;

    }


}
