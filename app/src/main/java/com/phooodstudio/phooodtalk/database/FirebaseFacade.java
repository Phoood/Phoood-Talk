package com.phooodstudio.phooodtalk.database;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phooodstudio.phooodtalk.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by Chris on 07-Jul-16.
 * For organization
 */
public abstract class FirebaseFacade {

    public static final String TAG = "Firebase Facade";

    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    public FirebaseFacade() {

        //Initialize Storage
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReferenceFromUrl("gs://phooodtalk.appspot.com");

        //Initialize databases
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }


    public DatabaseReference getDatabaseReference(String... args) {
        DatabaseReference returnDatabase = mFirebaseDatabase.getReference();
        for (String var : args) {
            returnDatabase = returnDatabase.child(var);
        }

        return returnDatabase;
    }


    public StorageReference getStorageReference() {
        return mStorageReference;
    }


    /**
     * Sends the message to Firebase.
     * Uses the time sent as an ID
     *
     * @param message
     */
    public void sendMessageToFirebase(Message message, String chatId) {
        DatabaseReference messageRootRef = mFirebaseDatabase.getReference("message")
                .child(chatId)
                .child(message.getTimeSent() + "");
        messageRootRef.setValue(message);
    }


    public void sendFileToFirebase(Uri obj, final String... args) { //TODO: change this to what you need
        //Filepath
        StorageReference storage = mStorageReference.getRoot();
        for (String var : args) {
            storage = storage.child(var);
        }

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

    public void sendFileToFirebase(File obj, final String... args){
        Uri resource = Uri.fromFile(obj);
        sendFileToFirebase(resource, args);
    }

    //TODO: decide on a standard image directory
    public void sendImageToFirebase(Bitmap bitmap, int quality, final String...args){
        //Filepath
        StorageReference storage = mStorageReference.getRoot();
        for (String var : args) {
            storage = storage.child(var);
        }

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

//    /**
//     * Requests a file from storage, using the path given
//     *
//     * @param args - path to storage location
//     */
//    public File requestFileFromStorage(File externalCacheDir, final String... args) {
//        //Filepath
//        StorageReference storage = mStorageReference.getRoot();
//        for (String var : args) {
//            storage = storage.child(var);
//        }
//
//        //Create tempoary file in external cache dir
//        File tempFile = null;
//        try {
//            tempFile = File.createTempFile(args[args.length - 1], null, externalCacheDir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //Attempt retrieval on a different thread
//        storage.getFile(tempFile).addOnSuccessListener(
//                new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        Log.d(TAG, "File " + Arrays.toString(args) + " was successfully downloaded");
//                    }
//                }
//        ).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "File " + Arrays.toString(args) + " could not be downloaded. Log: \n");
//                e.printStackTrace();
//            }
//        });
//        return tempFile; //NO DONT ACTUALLY DO THIS, THIS IS ALL INCORRECT
//
//    }
}
