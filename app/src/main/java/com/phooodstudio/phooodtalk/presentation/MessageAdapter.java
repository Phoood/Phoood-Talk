package com.phooodstudio.phooodtalk.presentation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phooodstudio.phooodtalk.R;
import com.phooodstudio.phooodtalk.model.Message;

import java.util.ArrayList;

/**
 * Defines the adapter for Message class.
 */
public class MessageAdapter extends BaseAdapter {

    // the types for getItemViewType method
    private static final int TYPES = 2;
    private static final int TYPE_STRING = 0;
    private static final int TYPE_IMAGE = 1;

    // TODO: 7/16/2016 find more memory efficient way
    private ArrayList<Message> mItems = new ArrayList<>();

    // used to create new view for individual item in the view
    private LayoutInflater mInflater;

    /**
     * Takes Context object and saves reference of LayoutInflater.
     *
     * @param context the current context
     */
    public MessageAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Adds a Message to the adapter.
     *
     * @param msg the message to add
     */
    public void add(Message msg) {
        mItems.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            switch (getItemViewType(position)) {
                case TYPE_STRING:
                    v = mInflater.inflate(R.layout.message_chat, null);
                    break;
                case TYPE_IMAGE:
                    v = mInflater.inflate(R.layout.message_image, null);
            }
        }

        Message msg = (Message) getItem(position);

        if (msg != null) {
            Object content = msg.getContents();

            if (content instanceof String) {
                TextView textView = (TextView) v.findViewById(R.id.chat_content);
                textView.setText((String) content);
            }
            else if (content instanceof Bitmap) {
                ImageView imageView = (ImageView) v.findViewById(R.id.chat_content);
                imageView.setImageBitmap((Bitmap) content);
            }
        }

        return v;
    }

    @Override
    public int getViewTypeCount() {
        return TYPES;
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = (Message) getItem(position);
        Object content = msg.getContents();
        if (content instanceof String) {
            return 0;
        }
        else if (content instanceof Bitmap) {
            return 1;
        }
        return super.getItemViewType(position);
    }
}
