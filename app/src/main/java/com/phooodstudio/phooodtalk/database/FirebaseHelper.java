package com.phooodstudio.phooodtalk.database;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phooodstudio.phooodtalk.model.Message;
import com.phooodstudio.phooodtalk.request.FileRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by Chris on 07-Jul-16.
 * For organization
 */
public final class FirebaseHelper {

    public static final String TAG = "Firebase Facade";

    private static final String REFERENCE_URL = "gs://phooodtalk.appspot.com";

    private static FirebaseHelper instance = null;

    private StorageReference mStorageReference;

    /**
     * The constructor.
     */
    private FirebaseHelper() {
        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(REFERENCE_URL);
    }

    /**
     * Returns the singleton reference.
     *
     * @return the singleton reference
     */
    public static FirebaseHelper getInstance() {

        // create the singleton if none exists
        if (instance == null) {
            instance = new FirebaseHelper();
        }

        return instance;
    }

    /**
     * Returns the reference to the specified path in Firebase Database.
     *
     * @param args the path
     * @return reference to the Firebase Database specified by args
     */
    public DatabaseReference getDatabaseReference(String... args) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        for (String var : args) {
            reference = reference.child(var);
        }

        return reference;
    }

    /**
     * Returns the reference to the specified path in Firebase Storage.
     *
     * @param args the path
     * @return reference to the Firebase Storage specified by args
     */
    public StorageReference getStorageReference(String... args) {
        StorageReference reference = mStorageReference.getRoot();
        for (String var : args) {
            reference = reference.child(var);
        }

        return reference;
    }


    /**
     * Sends the message to Firebase.
     * Uses the time sent as an ID.
     *
     * @param message the message to send
     * @param chatId  the chat id
     */
    public void sendMessage(Message message, String chatId) {
        DatabaseReference messageRootRef = getDatabaseReference(
            "message",
            chatId,
            message.getTimeSent()
        );
        messageRootRef.setValue(message);
    }

    /**
     * Sends an arbitrary local file pointed by obj to Firebase.
     *
     * @param obj  the file to send
     * @param args the file path to store in the Firebase Storage
     */
    public void sendFile(Uri obj, final String... args) {

        //Filepath
        StorageReference storage = getStorageReference(args);

        UploadTask uploadTask = storage.putFile(obj);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error uploading file " + Arrays.toString(args));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Uploaded file " + Arrays.toString(args));
            }
        });

    }

    public void sendFile(File obj, final String... args){
        Uri resource = Uri.fromFile(obj);
        sendFile(resource, args);
    }

    //TODO: decide on a standard image directory
    public void sendImage(Bitmap bitmap, int quality, final String... args) {

        //Filepath
        StorageReference storage = getStorageReference(args);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
        byte[] output = outputStream.toByteArray();

        UploadTask uploadTask = storage.putBytes(output);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error uploading file " + Arrays.toString(args));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Uploaded file " + Arrays.toString(args));
            }
        });
    }

    /**
     * Requests a file from storage, using the path given
     *
     */
    public void requestFile(final FileRequest request) {
        final String[] args = request.getFilePath();
        File externalCacheDir = request.getCacheDir();

        //Filepath in Firebase Storage
        StorageReference storage = mStorageReference.getRoot();
        for (String var : args) {
            storage = storage.child(var);
        }

        //Create temporary file in external cache dir
        try {
            final File tempFile = File.createTempFile(args[args.length - 1], null, externalCacheDir);

            //Attempt retrieval on a different thread
            storage.getFile(tempFile).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "File " + Arrays.toString(args) + " was successfully downloaded");

                            //Notify request is successful
                            request.notifyRequestSuccessful(tempFile);
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "File " + Arrays.toString(args) + " could not be downloaded. Log: \n");
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
