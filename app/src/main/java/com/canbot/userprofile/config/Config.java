package com.canbot.userprofile.config;

import android.os.Environment;

/**
 * Created by ${ping} on 2018/9/10.
 */
public class Config {

    public static final int PRINT_DEBUG=0;//是否打印log 标志
    public static final String LOG_PATH= Environment.getExternalStorageDirectory().getPath()+"/"+"canbot/profile.log";
    public static final long FILE_MAX_SIZE = 1024 * 1024 * 1;//log 文件的最大容量
}
