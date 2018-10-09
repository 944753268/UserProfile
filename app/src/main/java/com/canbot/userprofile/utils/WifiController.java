package com.canbot.userprofile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.canbot.userprofile.application.MyApplication;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;

/**
 * Created by chaffee on 2018/7/24.
 */

public class WifiController {

    private String TAG = "voice_Wifi_";

    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;

    private static WifiController wifiController;

    /**
     * wifi加密类型
     */
    public enum WifiEncryptionType {
        WIFI_ENCRYPTION_TYPE_NOPASSWORD(0), WIFI_ENCRYPTION_TYPE_WEP(1), WIFI_ENCRYPTION_TYPE_WPA(2), WIFI_ENCRYPTION_INVALID(3);
        private int value;

        WifiEncryptionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 获取WifiController单例
     *
     * @return
     */
    public static WifiController getInstance() {
        if (wifiController == null) {
            synchronized (WifiController.class) {
                if (wifiController == null) {
                    wifiController = new WifiController();
                }
            }
        }
        return wifiController;
    }

    private WifiController() {
        init(MyApplication.getContext());
    }

    private void init(Context context) {
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    /**
     * 接口:通过ssid和password连接wifi
     *
     * @param ssid
     * @param password
     * @return
     */
    public boolean connect(String ssid, String password) {
        Log.e(TAG, "connect, ssid= " + ssid + "pass= " + password);
        int encryptionType = getEncryptionType(mWifiManager, ssid);

        if (!this.setWifiEnabled()) {
            return false;
        }

        // 判断wifi状态是否可用
        while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            try {
                // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException ie) {
            }
        }

        // 是否配置过这个网络
        WifiConfiguration wifiConfiguration = this.getConfiguredNetworkBySsid(ssid);
        if (wifiConfiguration != null) {
            mWifiManager.removeNetwork(wifiConfiguration.networkId);
        }

        WifiConfiguration wifiConfig = createWifiConfig(ssid, password, encryptionType);

        // 添加一个新的网络描述为一组配置的网络。
        int netID = mWifiManager.addNetwork(wifiConfig);

        // 设置为true,使其他的连接断开
        boolean mConnectConfig = mWifiManager.enableNetwork(netID, true);
        mWifiManager.saveConfiguration();

        return mConnectConfig;
    }


    /**
     * 根据ssid获取加密类型
     *
     * @param mWifiManager
     * @param ssid
     * @return
     */
    private int getEncryptionType(WifiManager mWifiManager, String ssid) {
        List<ScanResult> list = mWifiManager.getScanResults();
        for (ScanResult scResult : list) {
            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {

                //获取wifi加密方式
                String capabilities = scResult.capabilities;
                Log.e(TAG, "capabilities=" + capabilities);
                if (!TextUtils.isEmpty(capabilities)) {
                    if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                        Log.e(TAG, "wpa");
                        return WifiEncryptionType.WIFI_ENCRYPTION_TYPE_WPA.getValue();
                    } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                        Log.e(TAG, "wep");
                        return WifiEncryptionType.WIFI_ENCRYPTION_TYPE_WEP.getValue();
                    } else {
                        Log.e(TAG, "no");
                        return WifiEncryptionType.WIFI_ENCRYPTION_TYPE_NOPASSWORD.getValue();
                    }
                } else {
                    return WifiEncryptionType.WIFI_ENCRYPTION_TYPE_NOPASSWORD.getValue();
                }
            }
        }
        return WifiEncryptionType.WIFI_ENCRYPTION_TYPE_NOPASSWORD.getValue();
    }

    /**
     * 设置wifi可用
     *
     * @return
     */
    public boolean setWifiEnabled() {
        boolean bReturn = true;
        if (!mWifiManager.isWifiEnabled()) {
            bReturn = mWifiManager.setWifiEnabled(true);
        }
        return bReturn;
    }

    /**
     * 获取已配置过的ssid网络配置数据
     *
     * @param ssid
     * @return
     */
    private WifiConfiguration getConfiguredNetworkBySsid(String ssid) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs == null || existingConfigs.size() == 0) {
            return null;
        }
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals(ssid) || existingConfig.SSID.equals("\"" + ssid + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 创建新的网络配置
     *
     * @param ssid
     * @param password
     * @param type
     * @return
     */
    private WifiConfiguration createWifiConfig(String ssid, String password, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = ssid;

        if (type == WifiEncryptionType.WIFI_ENCRYPTION_TYPE_NOPASSWORD.getValue()) {
            config.preSharedKey = null;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        if (type == WifiEncryptionType.WIFI_ENCRYPTION_TYPE_WEP.getValue()) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }

        if (type == WifiEncryptionType.WIFI_ENCRYPTION_TYPE_WPA.getValue()) {
            // 修改之后配置
            config.preSharedKey = "\"" + password + "\"";
        }
        return config;
    }

    /**
     * 判断wifi是否连接
     *
     * @return
     */
    public static boolean blNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 判断是否联网
     *
     * @return
     */
    public static boolean blNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 3 www.baidu.com");
            int exitValue = ipProcess.waitFor();
            Log.e("Avalible", "Process:" + exitValue);
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
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
}
