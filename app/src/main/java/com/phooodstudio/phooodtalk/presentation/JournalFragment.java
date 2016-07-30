package com.phooodstudio.phooodtalk.presentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phooodstudio.phooodtalk.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

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
    private File mDownloadFile;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);
        mContext = getActivity();

//        final FirebaseStorage storage = FirebaseStorage.getInstance();
//        mStorageReference = storage.getReferenceFromUrl("gs://phooodtalk.appspot.com");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("sharedPicture");

        //Create directory to store image
        try {
            super.onCreate(savedInstanceState);
            File root = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "myDir" + File.separator);
            //noinspection ResultOfMethodCallIgnored
            root.mkdirs();
            mImageFile = new File(root, "sharedPicture");
            mDownloadFile = new File(root, "downloadPicture");
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

//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String filename = dataSnapshot.getValue(String.class);
//                if (filename != null || !filename.equals("")) {
//                    StorageReference downloadReference = mStorageReference.child(filename);
//                    final Uri downloadURI = Uri.fromFile(mDownloadFile);
//                    downloadReference.getFile(mDownloadFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            try {
//                                Bitmap loadBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), downloadURI);
//                                mImageView.setImageBitmap(loadBitmap);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "Failed to read value.", databaseError.toException());
//            }
//        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        //Get the picture, to database
        if (requestCode == REQUEST_TAKE_PHOTO) {
            Log.d(TAG, "request code: request take photo");

            //Send picture to the storage
            try {
                InputStream stream = new FileInputStream(mImageFile);
                UploadTask uploadTask = mStorageReference.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "unuccessful upload");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Send notification to database that there was success
                        mDatabaseReference.setValue(new Date().getTime());
                        Log.d(TAG, "successful upload");
                    }
                });
            } catch (FileNotFoundException e) {
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
