package com.canbot.userprofile.broadcastReviver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.canbot.userprofile.application.MyApplication;
import com.canbot.userprofile.voice.FileUploadService;

/**
 * Created by ${ping} on 2018/9/26.
 */
public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("TAG",intent.getExtras().getString("facetoken"));

        MyApplication.Facetoken=intent.getExtras().getString("facetoken");
        MyApplication.flag=1;

        //开启上传线程
        Intent intent1 = new Intent(MyApplication.getContext(), FileUploadService.class);
        intent1.putExtra("facetoken", intent.getExtras().getString("facetoken"));
        MyApplication.getContext().startService(intent1);




    }
}
