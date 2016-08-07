package com.phooodstudio.phooodtalk.presentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.phooodstudio.phooodtalk.R;
import com.phooodstudio.phooodtalk.database.FacebookHelper;
import com.phooodstudio.phooodtalk.model.Account;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 * Defines the fragment to show the friends list
 */
public class FriendsFragment extends Fragment {

    public static final String TAG = "FriendsFragment";
    public static final String ACCOUNT_EXTRA = "Account extra ajkaak3802wtjgnw";
    private FriendsListAdapter mAdapter;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        mListView = (ListView) view.findViewById(R.id.fragment_friends_list_view);

        ArrayList<Account> accounts = new ArrayList<>();
        mAdapter = new FriendsListAdapter(getContext(), accounts);
        mListView.setAdapter(mAdapter);

        /* make the API call */
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/friends", null,
                HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                try {
                    if (response != null) {
                        //Get the full friend array and add it to an arraylist
                        //For those wondering, /me/friends will only get friends with the app installed
                        Log.d(TAG, "response: " + response.toString());

                        JSONArray friendArray = response.getJSONObject().getJSONArray("data"); //1 is the index of the array
                        Log.d(TAG, "friendArray: " + friendArray.toString());

                        for (int index = 0; index < friendArray.length(); index++){
                            Account newAccount = new Account();
                            newAccount.setName(friendArray.getJSONObject(index).getString("name"));
                            newAccount.setId(friendArray.getJSONObject(index).getString("id"));
                            mAdapter.add(newAccount);
                        }

                        Log.d(TAG, "Amount of friends added: " + mAdapter.getCount());

                        //Call on UI thread. how?
                        //TODO: THIS IS BAD. REALLY BAD. DONT START A NEW THREAD HERE
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                Log.d(TAG, "mAdapter notifyDataSetChanged called");
                            }
                        });

                    } else {
                        Log.d(TAG, "you have no friends");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).executeAsync();

        return view;
    }


    /**
     * Adapter
     */
    public class FriendsListAdapter extends ArrayAdapter<Account> {
        public FriendsListAdapter(Context context, List<Account> objects) {
            super(context, R.layout.fragment_friends_account, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View returnView;
            final int finalPosition = position;

            if (convertView != null) {
                returnView = convertView;
            } else {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                returnView = inflater.inflate(R.layout.fragment_friends_account, null);
            }

            //Add elements
            final ImageView imageView = (ImageView) returnView.findViewById(
                    R.id.fragment_friends_account_picture);
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageView.setMaxWidth(imageView.getHeight());
                }
            }, 300);

            //Set profile pic and text
            new FacebookHelper.RetrieveImageTask(imageView).execute(getItem(position));
            TextView nameView = (TextView) returnView.findViewById(R.id.fragment_friends_account_name);
            nameView.setText(getItem(position).getName());

            //Set regular tap behavior
            returnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra(ACCOUNT_EXTRA, getItem(finalPosition));
                    startActivity(intent);
                }
            });

            //Set button click behavior
            Button button = (Button) returnView.findViewById(
                    R.id.fragment_friends_account_select_profile);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent friendActivityIntent = new Intent(getActivity(), FriendActivity.class);
                    friendActivityIntent.putExtra(ACCOUNT_EXTRA, getItem(finalPosition));
                    startActivity(friendActivityIntent);
                }
            });

            return returnView;
        }
    }


}
