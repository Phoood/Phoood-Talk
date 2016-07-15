package com.phooodstudio.phooodtalk.presentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phooodstudio.phooodtalk.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Christopher Cabreros on 27-Jun-16.
 * Defines the fragment to show the friends list
 */
public class JournalFragment extends Fragment {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = "JournalFragment";
    private static final int COMPRESSION = 25; //out of 100

    private ImageView mImageView;
    private Button mButton;
    private File mImageFile;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("sharedPicture");

        //Create directory
        try {
            super.onCreate(savedInstanceState);
            File root = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "myDir" + File.separator);
            //noinspection ResultOfMethodCallIgnored
            root.mkdirs();
            mImageFile = new File(root, "picture");
        } catch (Exception e) {
            System.err.println("Unable to create file for storage");
            e.printStackTrace();
        }

        mImageView = (ImageView) view.findViewById(R.id.fragment_journal_image_view);
        mButton = (Button) view.findViewById(R.id.fragment_journal_take_picture);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create URI for photo sharing
                Uri outputFileUri = Uri.fromFile(mImageFile);
                Intent cameraActivityIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                cameraActivityIntent.setFlags(0);
                startActivityForResult(cameraActivityIntent, REQUEST_TAKE_PHOTO);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Bitmap bitmap = stringToBitmap(value);
                mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        //Get the picture, to database
        if (requestCode == REQUEST_TAKE_PHOTO) {
            Log.d(TAG, "request code: request take photo");
            Uri receivedUri = Uri.fromFile(mImageFile);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), receivedUri);
                Bitmap rotatedBitmap = rotateBitmap(bitmap, 270);
                String bitmapString = bitmapToString(rotatedBitmap);
                myRef.setValue(bitmapString);

                bitmap.recycle();
                rotatedBitmap.recycle();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESSION, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public static Bitmap stringToBitmap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return rotatedBitmap;
    }

}
