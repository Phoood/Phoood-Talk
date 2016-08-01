package com.phooodstudio.phooodtalk.presentation;

import android.content.Context;
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

    // TODO: 7/16/2016 find more memory efficient way
    private ArrayList<Message> mItems = new ArrayList<>();

    // used to create new view for individual item in the view
    private LayoutInflater mInflater;
    private String mUserId;

    /**
     * Takes Context object and saves reference of LayoutInflater.
     *
     * @param context the current context
     */
    public MessageAdapter(Context context, String id) {
        mInflater = LayoutInflater.from(context);
        mUserId = id;
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

                case Message.TYPE_STRING:
                    currentView = mInflater.inflate(R.layout.message_chat_incoming, parent, false);
                    break;

                case Message.TYPE_IMAGE:
                    currentView = mInflater.inflate(R.layout.message_image_incoming, parent, false);
                    break;

                case Message.TYPE_RATING:
                    // TODO: implement rating
                    break;

                case Message.TYPE_STRING + Message.TYPES:
                    currentView = mInflater.inflate(R.layout.message_chat_outgoing, parent, false);
                    break;

                case Message.TYPE_IMAGE + Message.TYPES:
                    currentView = mInflater.inflate(R.layout.message_image_outgoing, parent, false);
                    break;

                case Message.TYPE_RATING + Message.TYPES:
                    // TODO: implement rating
                    break;
            }
        }

        // get the message item
        Message msg = (Message) getItem(position);

        // making sure we don't get null exception
        if (currentView != null && msg != null) {

            // set the content according the message content

            View contentView = currentView.findViewById(R.id.chat_content);

            if (contentView != null) {
                switch (msg.getType()) {

                    case Message.TYPE_STRING:
                        ((TextView) contentView).setText(msg.getContents());
                        break;

                    case Message.TYPE_IMAGE:
                        // TODO: find way to retrieve image from local or server storage
                        break;

                    case Message.TYPE_RATING:
                        // TODO: implement rating
                        break;
                }
            }
        }

        return currentView;
    }

    @Override
    public int getViewTypeCount() {
        return Message.TYPES * 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = (Message) getItem(position);

        int viewType = msg.getType();

        if (mUserId.equals(((Message) getItem(position)).getSender())) {
            // outgoing types are added by number of types
            viewType += Message.TYPES;
        }

        return viewType;
    }
}
