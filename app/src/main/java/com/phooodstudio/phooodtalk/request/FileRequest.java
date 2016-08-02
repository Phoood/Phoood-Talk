package com.phooodstudio.phooodtalk.request;

import com.phooodstudio.phooodtalk.presentation.ChatActivity;

import java.io.File;

/**
 * Created by Chris on 01-Aug-16.
 *
 * Run onRequestSuccessful to specify what the request should do after
 */
public abstract class FileRequest {

    private String[] mFilePath;
    private File mCacheDir;
    private File mReturnFile;

    public FileRequest(File cacheDir, String[] filePath ) {
        mCacheDir = cacheDir;
        mFilePath = filePath;
    }

    public String[] getFilePath() {
        return mFilePath;
    }

    public File getCacheDir() {
        return mCacheDir;
    }

    public File getReturnFile() {
        return mReturnFile;
    }

    public abstract void onRequestSuccessful();

    public void notifyRequestSuccessful(File returnFile){
        mReturnFile = returnFile;
        onRequestSuccessful();
    }
}
