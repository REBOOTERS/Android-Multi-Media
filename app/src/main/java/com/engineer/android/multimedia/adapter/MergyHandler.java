package com.engineer.android.multimedia.adapter;

import java.io.File;

import heart.fun.creator.handler.CreatorExecuteResponseHander;

/**
 * @author zhuyongging @ Zhihu Inc.
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
//        new File(sPath).renameTo(dfile);
        // TODO: 2019-09-18  copy file
//        FileUtil.copyFile(sPath, dPath);
    }
}
