package com.yyq.cat;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ContactNotifyEvent;

/**
 * Author: TiAmo
 * Date: 2020/8/24 8:14
 * Description:
 */
public class MyApplication extends Application {

    public static String APPKEY="067e5a4e8c6fd432307735e1";
    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.init(this,true);
        JMessageClient.setDebugMode(true);
    }
}
