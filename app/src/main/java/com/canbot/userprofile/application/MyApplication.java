package com.canbot.userprofile.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by ${ping} on 2018/9/10.
 */
public class MyApplication extends Application {
    private static Context mContext;

    public static String Facetoken="";
    public static int flag=0;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
