package com.yyq.cat;

import android.app.Application;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Author: TiAmo
 * Date: 2020/8/24 8:14
 * Description:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JMessageClient.init(this,true);
        JMessageClient.setDebugMode(true);
    }
}
