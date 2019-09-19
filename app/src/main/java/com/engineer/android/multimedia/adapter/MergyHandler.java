package com.engineer.android.multimedia.adapter;

import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import heart.fun.creator.handler.CreatorExecuteResponseHander;

/**
 * @author rookie
 * @since 09-18-2019
 */
public class MergyHandler implements CreatorExecuteResponseHander {

    String sPath;
    String dPath;

    public MergyHandler(String sPath, String dPath) {
        this.sPath = sPath;
        this.dPath = dPath;
    }

    @Override
    public void onSuccess(Object message) {

    }

    @Override
    public void onProgress(Object message) {
        Log.e("MeidaCodec", "onProgress: "+message );
    }

    @Override
    public void onFailure(Object message) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {
        File dfile = new File(dPath);
        if (dfile.exists()) {
            dfile.delete();
        }

        try {
            FileUtils.copyFile(new File(sPath),dfile);
            Log.e("creator", "onFinish: "+dfile.getAbsolutePath() );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
