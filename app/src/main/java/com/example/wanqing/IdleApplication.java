package com.example.wanqing;

import android.app.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

/**
 * Created by dahuahua on 2017/4/27.
 */

public class IdleApplication extends Application {
    private static final String key = "51ed94e6fe52d2386a54f036aa991298";

    private static IdleApplication INSTANCE;

    public static final int LOGIN = 1;
    public static final int LOGOUT = 4;
    public static final int UPDATE_USER_INFO = 2;
    public static final int REQUEST_CAMERA = 3;
    public static final int UPDATE_IDLE_LIST = 5;
    public static final int ITEM_SEND_TEXT =6;
    public static final int ITEM_RECEIVE_TEXT =7;

    public static final int REQUEST_CODE = 8;
    public static final int RESULT_CODE = 9;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = new IdleApplication();

        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            //NewIM初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new MessageHandler(this));
        }

    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IdleApplication INSTANCE() {
        return INSTANCE;
    }
}
