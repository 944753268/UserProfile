package com.canbot.userprofile.voice;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.canbot.userprofile.network.FTPUtils;
import com.canbot.userprofile.network.HttpUtils;
import com.canbot.userprofile.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${ping} on 2018/9/27.
 */
public class FileUploadService extends IntentService {

    private static final String USERDIR = "/sdcard/canbot/userprofile/";
    private static final String URL = "canbot.vip";
//    private static final String URL = "canbot.net.cn";
    private static final String USERNAME = "liguolin";
//    private static final String PASSW = "Liguolin123";
    private static final String PASSW = "EVKKlGy3";
    private static final String ftpDir = "/UserProfile/";

    public FileUploadService() {
        super("down");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String getface_token = intent.getExtras().getString("facetoken");//当前传过来的facetoken


        Utils.debugger("修改完成后，开始上传");

        /*********遍历目录下面的文件夹***************/
        File file = new File(USERDIR);
        File[] subFile = file.listFiles();
        for (int i = 0; i < subFile.length; i++) {
            String faceToken = subFile[i].getName();
            Utils.debugger("文件夹的名字："+faceToken);
            if (faceToken.equals(getface_token) || faceToken.equals("data.txt"))
                continue;

            String faceJpgPath = USERDIR + faceToken + "/" + "face.jpg";
            String actionPath = USERDIR + faceToken + "/" + "action.txt";
            String voicePath = USERDIR + faceToken + "/" + "voice.txt";
            Utils.debugger(actionPath);
            //ftp 上传文件到服务器
            FTPUtils ftpUtils = FTPUtils.getInstance();
            String timeStamp=Utils.getTimeStamp();//获取当前的时间戳
            if (Utils.isfileexist(faceJpgPath))
            ftpUtils.uploadFtpFacejpg(URL,21,USERNAME,PASSW,faceJpgPath,ftpDir+faceToken+"/","face.jpg",Utils.getDay());
            //当文件存在的时候则开始上传
            if (Utils.isfileexist(actionPath))
                ftpUtils.uploadFtpFile(URL, 21, USERNAME, PASSW, actionPath, ftpDir + faceToken + "/", timeStamp+".action.",Utils.getDay());
            if (Utils.isfileexist(voicePath))
                ftpUtils.uploadFtpFile(URL, 21, USERNAME, PASSW, voicePath, ftpDir + faceToken + "/", timeStamp+".voice",Utils.getDay());

            Utils.debugger("上传完成后，开始写进数据库");
            //上传成功到服务器数据库
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String nowTime = df.format(new Date());// new Date()为获取当前系统时间
            Map<String, String> params = new HashMap<String, String>();
            params.put("face", nowTime);
            params.put("voice", nowTime);
            params.put("action", nowTime);
            String strUrlPath = "http://canbot.vip:8083/api/v1/userInfo/" + faceToken;
            HttpUtils.submitPostData(strUrlPath, params, "utf-8");
            //删除目录
            Utils.ExecCmd("cd " + USERDIR + " &&  rm -rf " + faceToken);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestory");
    }
}
