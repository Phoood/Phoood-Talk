package com.phooodstudio.phooodtalk.presentation;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phooodstudio.phooodtalk.R;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 * Defines the fragment to show the friends list
 */
public class FriendsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }
}
