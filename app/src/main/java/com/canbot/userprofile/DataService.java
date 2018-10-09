package com.canbot.userprofile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.canbot.userprofile.action.ActionThread;
import com.canbot.userprofile.utils.Utils;
import com.canbot.userprofile.voice.ReceiveVoiceService;

/**
 * Created by ${ping} on 2018/9/26.
 */
public class DataService extends Service {
    private Context context;

    @Override
    public void onCreate() {

        context = this;
        super.onCreate();
        Utils.createFileDir("/sdcard/canbot/userprofile/");
        Utils.createFilePath("/sdcard/canbot/userprofile/data.txt");

        Utils.ExecCmd("mkdir -p /sdcard/canbot/userprofile/");
        ActionThread actionThread = new ActionThread();
        actionThread.start();
        Intent intent = new Intent(this, ReceiveVoiceService.class);
        startService(intent);


        new Thread() {
            @Override
            public void run() {
                super.run();
//            Map<String, String> params = new HashMap<String, String>();
//
//                params.put("voice","1000");
//                params.put("action","1000");
//
//            String strUrlPath = "http://canbot.vip:8083/api/v1/userInfo/sssssss";
//            String strResult = HttpUtils.submitPostData(strUrlPath, params, "utf-8");

//            String str = HttpUtils.getFacetoken("http://canbot.vip:8083/api/v1/userInfo/sssssss");
//               Log.e("TAG",str);

//                FTPUtils.getInstance().ftpDown("192.168.2.199",10021,"liguolin","Liguolin123",
//                        "/sdcard/canbot/userprofile/","/UserProfile/demo.txt","demo.txt");
//                FTPUtils.getInstance().uploadFtpFile("192.168.2.199",10021,"liguolin","Liguolin123",
//                        "/sdcard/canbot/userprofile/PZ3SmzV6taECqEeY4oAb-w==/face.jpg","/UserProfile/PZ3SmzV6taECqEeY4oAb-w==/","face.jpg");
            }
        }.start();


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
