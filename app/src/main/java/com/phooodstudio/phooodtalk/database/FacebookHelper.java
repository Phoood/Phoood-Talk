package com.phooodstudio.phooodtalk.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.phooodstudio.phooodtalk.model.Account;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Chris on 05-Aug-16.
 * Helper class for facebook
 */
public class FacebookHelper {

    public static final String TAG = "FacebookHelper";

    private static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");

        Log.d(TAG, "Receiving profile picture from " + userID);

        return BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
    }


    /**
     * Helper class to retrieve a facebook image
     */
    public static class RetrieveImageTask extends AsyncTask<Account, Void, Bitmap> {

        private ImageView mEditView;

        public RetrieveImageTask(ImageView editView) {
            super();
            mEditView = editView;
        }

        @Override
        protected Bitmap doInBackground(Account... params) {
            Bitmap profileBitmap = null;
            try {
                profileBitmap = FacebookHelper.getFacebookProfilePicture(params[0].getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return profileBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            mEditView.setImageBitmap(bitmap);
            mEditView.post(new Runnable() {
                @Override
                public void run() {
                    mEditView.setMaxWidth(mEditView.getHeight());
                }
            });

            Log.d(TAG, "Task completed");
        }
    }
}
