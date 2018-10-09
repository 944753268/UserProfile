package com.canbot.userprofile.voice;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.alibaba.fastjson.JSON;
import com.canbot.u05.IMsgBind;
import com.canbot.u05.IMsgCallBack;
import com.canbot.userprofile.application.MyApplication;
import com.canbot.userprofile.config.Config;
import com.canbot.userprofile.model.MsgType;
import com.canbot.userprofile.model.Sensor;
import com.canbot.userprofile.model.StringMsgBean;
import com.canbot.userprofile.utils.FileUtils;
import com.canbot.userprofile.utils.Utils;
import com.canbot.userprofile.utils.WifiController;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ReceiveVoiceService extends Service {
    private String nowDate;

    private static final String USERPROFILEDIR = "/sdcard/canbot/userprofile/";

    String nowTime;
    String str;

    public ReceiveVoiceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterAIDL();
        unBindMsgService();


    }

    /**
     * 处理收到的结果
     *
     * @param msgType 消息类型
     * @param msgData 消息内容
     * @return true 客户端需要处理,可以根据识别结果自定义处理(访问自己的语义服务器，或者本地处理).
     * false 客户端不处理,返回给机器人使用机器人默认的语义服务器.
     */
    /*每个记录都要时间*/



    private boolean handleResult(int msgType, String msgData) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        nowTime = df.format(new Date());// new Date()为获取当前系统时间
//        Log.e("TAG", "收到的结果：" + msgData);
        if (msgType == MsgType.RECEIVER_MSG_WAKE_UP) {

            String faceToke = MyApplication.Facetoken;
            if (faceToke != null && MyApplication.flag == 1) {
                Utils.createFileDir(USERPROFILEDIR + faceToke+"/");
                String fileVociePath = USERPROFILEDIR + faceToke + "/voice.txt";
                Utils.createFilePath(fileVociePath);
                Utils.print_data(Config.LOG_PATH, "收到语音识别结果: " + msgData);
                msgData = nowTime + "," + "收到头部唤醒" + "\n";
                FileUtils.writeFileFromString(fileVociePath, msgData, true);
            }
            sendToRobot(MsgType.PLAY_TTS, "我在呢" + "&&recoder");
            sendToRobot(MsgType.RECEIVER_WAKEUP_SOURCE_ROTATE, msgData);

            return true;
        } else if (msgType == MsgType.RECEIVER_MSG_VOICE_RECOGNIZER_RESULT) {


            String faceToke = MyApplication.Facetoken;
            if (faceToke != null &&  MyApplication.flag == 1) {
                Utils.createFileDir(USERPROFILEDIR + faceToke+"/");
                String fileVociePath = USERPROFILEDIR + faceToke + "/voice.txt";
                Utils.createFilePath(fileVociePath);
                Utils.print_data(Config.LOG_PATH, "收到语音识别结果: " + msgData);
                msgData = nowTime + "," + msgData + "\n";
                FileUtils.writeFileFromString(fileVociePath, msgData, true);

            }


            return true;
        } else if (msgType == MsgType.RECEIVER_MSG_SENSOR) {
            Utils.print_data(Config.LOG_PATH, "收到感应器");
            int receiveSensorType = Integer.parseInt(msgData);
            if (receiveSensorType == Sensor.Belly) {
                Utils.print_data(Config.LOG_PATH, "肚子感应器被触摸: ");
                String saveData = nowTime + "," + "肚子感应器被触摸" + "\n";
//                FileUtils.writeFileFromString(FILE_VOICE_PATH_TODAY, saveData, true);
            } else if (receiveSensorType == Sensor.Buns) {
                Utils.print_data(Config.LOG_PATH, "屁股感应器被触摸: ");
                String saveData = nowTime + "," + "屁股感应器被触摸" + "\n";
//                FileUtils.writeFileFromString(FILE_VOICE_PATH_TODAY, saveData, true);
            } else if (receiveSensorType == Sensor.Right_ear) {
                Utils.print_data(Config.LOG_PATH, "右耳感应器被触摸: ");
                String saveData = nowTime + "," + "右耳感应器被触摸" + "\n";
//                FileUtils.writeFileFromString(FILE_VOICE_PATH_TODAY, saveData, true);
            } else if (receiveSensorType == Sensor.Mouth) {
                Utils.print_data(Config.LOG_PATH, "下巴感应器被触摸: ");
                String saveData = nowTime + "," + "下巴感应器被触摸" + "\n";
//                FileUtils.writeFileFromString(FILE_VOICE_PATH_TODAY, saveData, true);
            } else if (receiveSensorType == Sensor.Head_top) {
                Utils.print_data(Config.LOG_PATH, "头顶感应器被触摸: ");
                String saveData = nowTime + "," + "头顶感应器被触摸" + "\n";
//                FileUtils.writeFileFromString(FILE_VOICE_PATH_TODAY, saveData, true);
            } else if (receiveSensorType == Sensor.Left_ear) {
                Utils.print_data(Config.LOG_PATH, "左耳感应器被触摸: ");
                String saveData = nowTime + "," + "左耳感应器被触摸" + "\n";
//                FileUtils.writeFileFromString(FILE_VOICE_PATH_TODAY, saveData, true);
            }


            String faceToke = MyApplication.Facetoken;
            if (faceToke != null &&  MyApplication.flag == 1) {
//                Utils.createFileDir(USERPROFILEDIR + faceToke+"/");
//                String fileVociePath = USERPROFILEDIR + faceToke + "/voice.txt";
//                Utils.createFilePath(fileVociePath);

                msgData = nowTime + "," + msgData + "\n";
//                FileUtils.writeFileFromString(fileVociePath, msgData, true);

            }
            return true;
        } else if (msgType == MsgType.RECEIVE_MSG_WIFI_SSID_AND_PWD) {
            Utils.print_data(Config.LOG_PATH, "接收到头部WIFI信息");
            Utils.print_data(Config.LOG_PATH, "wifi= " + msgData);
            Utils.print_data(Config.LOG_PATH, "type= " + msgType);
            if (msgData.contains(":")) {
                Utils.print_data(Config.LOG_PATH, "进入连接判断");
                String[] datas = msgData.split(":");
                final String ssid = datas[0];

                Utils.print_data(Config.LOG_PATH, "ssid= " + ssid);
                Utils.print_data(Config.LOG_PATH, "datas.length= " + datas.length);

                if (datas.length > 1) {
                    final String pwd = datas[1];
                    Utils.print_data(Config.LOG_PATH, "pwd= " + pwd);
                    WifiController wifiController = WifiController.getInstance();
                    Utils.print_data(Config.LOG_PATH, "wifiUtils= " + wifiController);
                    boolean b = wifiController.connect(ssid, pwd);
                    if (b) {
                        Utils.print_data(Config.LOG_PATH, "网络连接成功");
                    } else {
                        Utils.print_data(Config.LOG_PATH, "网络连接失败");
                    }
                } else {
                    Utils.print_data(Config.LOG_PATH, "头部发送的 wifi 不正确");
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**************************************************/
    /**
     * 绑定远程服务
     * 请客户不要随意修改此处代码。
     */
    protected void bindService() {
        try {
            Intent intent = new Intent("com.canbot.u05.service.MsgService");
            intent.setClassName("com.canbot.u05", "com.canbot.u05.service.MsgService");
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            Utils.print_data(Config.LOG_PATH, "bindService-----Exception--" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 解绑远程服务
     */
    protected void unBindMsgService() {
        Utils.print_data(Config.LOG_PATH, "unBindMsgService");
        unbindService(connection);
    }

    /**
     * 绑定远程接口
     */
    private void registerAIDL() {
        if (mService != null) {
            try {
                mService.registerCallBack(iMsgCallback);
                Utils.print_data(Config.LOG_PATH, "registerAIDL---------");
            } catch (RemoteException e) {
                Utils.print_data(Config.LOG_PATH, "RemoteException-------" + e.toString());
            }
        }
    }

    /**
     * 解绑远程接口
     */
    protected void unRegisterAIDL() {
        if (mService != null) {
            try {
                mService.unRegisterCallBack(iMsgCallback);
                mService = null;
                Utils.print_data(Config.LOG_PATH, "unRegisterAIDL---------");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    private IMsgBind mService;

    protected ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Utils.print_data(Config.LOG_PATH, "onServiceDisconnected-------");

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Utils.print_data(Config.LOG_PATH, "onServiceConnected-------");
            mService = IMsgBind.Stub.asInterface(service);
            registerAIDL();
        }
    };


    /**
     * 远程服务回调接口，用于接收远程服务发送的消息
     * 请客户不要随意修改此处代码
     */
    private IMsgCallBack iMsgCallback = new IMsgCallBack.Stub() {
        @Override
        public boolean interceptRobotAction(final String dataJson) {
            Utils.print_data(Config.LOG_PATH, "onReceiver-------data=" + dataJson);
            StringMsgBean stringMsgBean = JSON.parseObject(dataJson, StringMsgBean.class);
            final int msgType = stringMsgBean.getMsgType();
            final String msgData = stringMsgBean.getMsgData();
            return handleResult(msgType, msgData);
        }
    };

    public void sendToRobot(int msgType, String data) {
        StringMsgBean msgBean = new StringMsgBean();
        msgBean.setMsgType(msgType);
        msgBean.setMsgData(data);
        sendData(msgBean);
    }

    /**
     * 通过aidl发送数据
     *
     * @param msgBean
     */
    protected void sendData(StringMsgBean msgBean) {
        if (mService != null) {
            String jsonString = JSON.toJSONString(msgBean);
            try {
                Utils.print_data(Config.LOG_PATH, "sendData=" + jsonString);
                mService.send(jsonString);
            } catch (RemoteException e) {
                e.printStackTrace();
                Utils.print_data(Config.LOG_PATH, "RemoteException=" + e.toString());
            }
        }
    }


}
