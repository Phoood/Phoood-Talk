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

    /*
     * the types for getItemViewType method
     * incoming and outgoing are based on this device
     * e.g. outgoing is messages sent, incoming is messages received
     */
    private static final int TYPES = 4;
    private static final int TYPE_STRING_INCOMING = 0;
    private static final int TYPE_STRING_OUTGOING = 1;
    private static final int TYPE_IMAGE_INCOMING = 2;
    private static final int TYPE_IMAGE_OUTGOING = 3;

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

        View currentView = convertView;

        if (currentView == null) {

            // inflate view according to the view types
            switch (getItemViewType(position)) {
                case TYPE_STRING_INCOMING:
                    currentView = mInflater.inflate(R.layout.message_chat_incoming, parent, false);
                    break;

                case TYPE_STRING_OUTGOING:
                    currentView = mInflater.inflate(R.layout.message_chat_outgoing, parent, false);
                    break;

                case TYPE_IMAGE_INCOMING:
                    currentView = mInflater.inflate(R.layout.message_image_incoming, parent, false);
                    break;

                case TYPE_IMAGE_OUTGOING:
                    currentView = mInflater.inflate(R.layout.message_image_outgoing, parent, false);
                    break;
            }
        }

        // get the message item
        Message msg = (Message) getItem(position);

        // making sure we don't get null exception
        if (currentView != null && msg != null) {
            Object content = msg.getContents();

            // set the content according the message content
            if (content instanceof String) {
                TextView textView = (TextView) currentView.findViewById(R.id.chat_content);
                textView.setText((String) content);
            }
            else if (content instanceof Bitmap) {
                ImageView imageView = (ImageView) currentView.findViewById(R.id.chat_content);
                imageView.setImageBitmap((Bitmap) content);
            }
        }

        return currentView;
    }

    @Override
    public int getViewTypeCount() {
        return TYPES;
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = (Message) getItem(position);
        Object content = msg.getContents();
        int viewType;

        if (content instanceof String) {
            viewType = TYPE_STRING_OUTGOING;
        }
        else if (content instanceof Bitmap) {
            viewType = TYPE_IMAGE_OUTGOING;
        }
        else {
            // TODO: 7/28/2016 this else statement should not be reached
            viewType = TYPE_STRING_OUTGOING;
        }

        // TODO: 7/28/2016 find way to check if the account is current one
        if (true /* just a place holder */) {
            // exploit the fact that the outgoing types are 1 more than incoming types
            ++viewType;
        }

        return viewType;
    }
}
