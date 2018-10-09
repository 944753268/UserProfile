package com.canbot.userprofile.utils;


import android.util.Log;

import com.canbot.userprofile.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ${ping} on 2018/7/16.
 */
public class Utils {

    public static void print_data(String path, String str) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long fileSize = getFileSize(file);
        //Log.d("text", "文件大小---" + fileSize);

        if (Config.PRINT_DEBUG == 0) {
            if (fileSize > Config.FILE_MAX_SIZE) {
                file.delete();
            }
            BufferedWriter bw = null;
            try {
                // 第二个参数意义是说是否以append方式添加内容
                bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(str);
                bw.write("\n");
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static long getFileSize(File file) {
        if (!isFileExists(file))
            return 0;
        return file.length();
    }

    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }


    /******执行命令************/
    public static void ExecCmd(String cmd) {
        Process process = null;
        DataOutputStream dataOutputStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes(cmd + "\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //读取文件数据
    public static String readOnFocuseActivity(String path) {
        BufferedReader br = null;
        String line = null;
        StringBuffer strbuf = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "utf-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                strbuf.append(line);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        return strbuf.toString();
    }

    public static void writeRecord(String filePath, String str) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str + "\r\n ");// 往已有的文件上添加字符串
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getProcessName(String activityName) {
        String RobotMoudle = "";

        Map<String, String> mymap = new HashMap<String, String>();
//        mymap.put("com.canbot.u05/.activity.WakeUpActivity", "待机");
        mymap.put("com.canbot.u05/.activity.StandbyActivity", "待机");
        mymap.put("com.canbot.u05/.activity.IndustryModActivity", "待机");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.SaleEnterActivity", "导购模式");
        mymap.put("com.canbot.u05/.activity.ushermode.UsherModeActivity", "门童接待");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.RouteActivity", "地点问询");
        mymap.put("com.canbot.u05/.activity.shoppingguide.merchandise.MerchandiseActivity", "商品导购");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.SalesPublicityActivity", "促销揽客");
        mymap.put("com.canbot.u05/.activity.shoppingguide.advisory.AdvisoryActivity", "咨询问答");
        mymap.put("com.canbot.u05/.activity.DancePerformActivity", "舞蹈表演");
        mymap.put("com.canbot.u05/.activity.AnimationActivity", "正在跳舞");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.ServiceActivity", "售后服务");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.ServiceActgeActivity", "会员管理");
        mymap.put("com.canbot.u05/.activity.shoppingguide.sales.PaymentActivity", "付款结帐");
        mymap.put("com.canbot.u05/.activity.mapguide.GuideEnterActivity", "导览模式");
        mymap.put("com.canbot.u05/.activity.mapguide.GuideNoTaskActivity", "场馆介绍");
        mymap.put("com.canbot.u05/.activity.mapguide.GuidePointerActivity", "路劲指引");
        mymap.put("com.canbot.u05/.activity.OfficeModeActivity", "办公模式");
        mymap.put("com.canbot.u05/.activity.vipguide.VipGuideActivity", "贵宾引导");
        mymap.put("com.canbot.u05/.activity.training.TaskListActivity", "培训主持");
        mymap.put("com.canbot.u05/.activity.presidemode.CompereNoTaskActivity", "主持");
        mymap.put("com.canbot.u05/.activity.RemoteVideoConnectActivity", "远程会议");
        mymap.put("com.interjoy.sksmarteyesdk_object_5/com.interjoy.sksmarteyesdk.MainActivity", "物体识别");
        mymap.put("com.canbot.u05/.activity.playvideo.VideoPlaybackActivit", "视频播放");
        mymap.put("com.canbot.u05/.activity.simultaneous.SimultaneousSelectAudioActivity", "互动同声");
        mymap.put("com.canbot.u05/.activity.bodytrack.BodyTrackActivity", "人体追踪");
        mymap.put("com.canbot.u05/.activity.meeting.MonitorMeetingConnectActivity", "会议追踪");
        mymap.put("com.canbot.u05/.activity.SettingActivity", "设置");
        mymap.put("com.canbot.u05/.activity.WifiActivity", "wifi设置");
        mymap.put("com.canbot.u05/.activity.BluetoothSetupActivity", "蓝牙设置");
        mymap.put("com.canbot.u05/.activity.mapguide.GuideMapRestoreActivity", "地图加载");
        mymap.put("com.canbot.u05/.activity.AboutActivity", "关于优友");
        mymap.put("com.canbot.u05/.activity.RobotIDActivity", "机器人ID");
        mymap.put("com.canbot.u05/.activity.RecoverCallActivity", "联系我们");
        mymap.put("com.canbot.u05/.activity.PowerInfoActivity", "电量查询");


        Iterator<Map.Entry<String, String>> ite = mymap.entrySet().iterator();
        while (ite.hasNext()) {

            Map.Entry str = (Map.Entry) ite.next();
            if (str.getKey().equals(activityName))
                RobotMoudle = (String) str.getValue();
        }

        return RobotMoudle;
    }


    public static String splitData(String str, String strStart, String strEnd) {
        String tempStr = "";
        String temp = "";
        String[] skt;
        if (str.length() < 2)
            return null;
        tempStr = str.substring(str.indexOf(strStart) + 1, str.lastIndexOf(strEnd));
        skt = tempStr.split(" ");
        if (skt.length > 2)
            temp = skt[2];
        return temp;
    }

    //读取文件数据
    public static void InitData(String path, List list) {
        list.clear();
        BufferedReader br = null;
        String line = null;

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "utf-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
    }

    /**
     * 获取当前机器的mac地址
     *
     * @return
     */
    public static String getLocalMacAddress() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        macSerial = macSerial.replaceAll(" ", "");
        String MAC = macSerial.replaceAll(":", "");
        return MAC;
    }

    /**
     * 将文件读取到byte流中
     *
     * @param path
     * @return
     */

    public static byte[] readFile(String path) {
        File file = new File(path);
        FileInputStream input = null;
        byte[] buf = new byte[0];
        try {
            input = new FileInputStream(file);
            buf = new byte[input.available()];
            input.read(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buf;
    }


    public static void debugger(String str) {

        Log.e("USERPROFILE", str);
    }

    public static void createFileDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createFilePath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean isfileexist(String path) {
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            return true;
        }
        return false;

    }

    public static String getDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳

    }

    /**
     * @return当前的时间戳
     */
    public static String getTimeStamp() {
        return String.valueOf(new Date().getTime() / 1000);
    }

}
