package com.canbot.userprofile.action;

import android.content.Context;

import com.canbot.userprofile.application.MyApplication;
import com.canbot.userprofile.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${ping} on 2018/7/16.
 */
public class ActionThread extends Thread {
    private Context context;

    public static final int TIME_SPAN = 5 * 1000;//时间间隔
    public static final String focusePath = "/sdcard/canbot/userprofile/data.txt";

    public static String recordPath = "/sdcard/canbot/userprofile/";

    public static String cmd = "dumpsys activity | grep mFocusedActivity > /sdcard/canbot/userprofile/data.txt";

    String activityData = "";
    String nowTime = "";
    String lastData = null;
    String writeDate = "";
    private String thriftControl = "thriftcontrol";
    private boolean thriftSuspend = false;
     String facetoken="";

    @Override
    public void run() {


        while (true) {


            try {
                Thread.sleep(TIME_SPAN);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


//            Utils.debugger("--------------"+MyApplication.Facetoken+MyApplication.flag);
            //获取栈顶的Activity名字
            Utils.ExecCmd(cmd);
            //获取命令得到的数据
            activityData = Utils.readOnFocuseActivity(focusePath);
            if (activityData.length() < 2)
                continue;
            //将获取的数据拆分，只取需要的部分
            activityData = Utils.splitData(activityData, "{", "}");
            if (activityData.length() < 2)
                continue;
            writeDate = Utils.getProcessName(activityData);
            if (writeDate.length() < 2)
                continue;
            //获取文件名字


            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            nowTime = df.format(new Date());// new Date()为获取当前系统时间


            String face_token = MyApplication.Facetoken;
            Utils.debugger(face_token+MyApplication.flag);


            if (writeDate.equals(lastData)) {
                //在同一个活动中不作任何事情
            } else {
                //把数据接写进文件


                if (MyApplication.flag == 1&& face_token.length()>2) {
               String dir=recordPath+face_token+"/";
                Utils.createFileDir(dir);
                String path=dir+"action.txt";
                Utils.createFilePath(path);
                    if (lastData == null)
                        Utils.writeRecord(path, nowTime + "," + "启动程序");
                    else
                        Utils.writeRecord(path, nowTime + "-----" + lastData + "结束");
                    Utils.writeRecord(path, nowTime + "-----" + writeDate + "启动");
                }
            }
            lastData = writeDate;
        }

    }





}
