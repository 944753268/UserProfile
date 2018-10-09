package com.canbot.userprofile.network;

import android.util.Log;

import com.canbot.userprofile.utils.Utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ${ping} on 2018/9/21.
 */
public class FTPUtils {
    public static FTPUtils ftpUtils;

    private FTPUtils() {

    }

    public static FTPUtils getInstance() {
        synchronized (FTPUtils.class) {
            if (ftpUtils == null) {
                ftpUtils = new FTPUtils();
            }
        }
        return ftpUtils;
    }

    public void ftpDown(String url, int port, String username, String password, String filePath, String FTP_file,
                        String SD_file) {
        FileOutputStream buffOut = null;
        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(url, port);
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
                ftpClient.enterLocalPassiveMode();
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                buffOut = new FileOutputStream(filePath + SD_file);
                InputStream input = ftpClient.retrieveFileStream(FTP_file);
                byte[] b = new byte[1024];
                int length = 0;
                while ((length = input.read(b)) != -1) {
                    buffOut.write(b, 0, length);
                }
                buffOut.flush();
                buffOut.close();
                input.close();

                Thread.sleep(1000);
                Utils.debugger("文件下载完成");

                ftpClient.logout();

            } else {
                Log.i("TAG", "ftp 登录失败");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
//                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
    }

    public  void uploadFtpFile(String url, int port, String username, String password, String sdcardFilePath, String desFileName, String filename,String day) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setConnectTimeout(6*1000);
            ftpClient.connect(url, port);
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录
//                ftpClient.setBufferSize(1024);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                ftpClient.mkd(desFileName);//创建facektoken ftp目录
                ftpClient.mkd(desFileName+day+"/");//创建当天的目录
                Utils.debugger("创建ftp服务器文件目录完成");
                FileInputStream srcFileStream = new FileInputStream(sdcardFilePath);
                Utils.debugger("ftp 服务器的文件名:"+filename);

                ftpClient.storeFile(desFileName+day+"/"+filename, srcFileStream);
                srcFileStream.close();
                Utils.debugger("文件上传完成");
                ftpClient.logout();
            } else {
                Utils.debugger("登录服务器失败");
            }

        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
//                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }

    }

    public  void uploadFtpFacejpg(String url, int port, String username, String password, String sdcardFilePath, String desFileName, String filename,String day) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setConnectTimeout(6*1000);
            ftpClient.connect(url, port);
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录
//                ftpClient.setBufferSize(1024);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();

               boolean isChange= ftpClient.changeWorkingDirectory(desFileName+day+"/");
               if(isChange){//目录存在
                   String[] fileNames = ftpClient.listNames();
                   List<String> pathList = Arrays.asList(fileNames);
                   if(!pathList.contains("face.jpg")){
                       FileInputStream srcFileStream = new FileInputStream(sdcardFilePath);
                       Utils.debugger("ftp 服务器的文件名:"+filename);
                       ftpClient.storeFile(desFileName+day+"/"+filename, srcFileStream);
                       srcFileStream.close();
                       Utils.debugger("文件上传完成");
                   }

               }else{//目录不存在
                   ftpClient.mkd(desFileName);//创建facektoken ftp目录
                   ftpClient.mkd(desFileName+day+"/");//创建当天的目录
                   Utils.debugger("创建ftp服务器文件目录完成");
                   FileInputStream srcFileStream = new FileInputStream(sdcardFilePath);
                   Utils.debugger("ftp 服务器的文件名:"+filename);
                   ftpClient.storeFile(desFileName+day+"/"+filename, srcFileStream);
                   srcFileStream.close();
                   Utils.debugger("文件上传完成");
               }


                ftpClient.logout();
            } else {
                Utils.debugger("登录服务器失败");
            }

        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
//                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }

    }
}
