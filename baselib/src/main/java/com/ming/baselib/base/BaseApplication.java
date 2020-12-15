package com.sming.LoveMing.base;

import android.app.Application;

public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    // 单例模式中获取唯一的ExitApplication 实例
    public static BaseApplication getInstance() {
        if (null == mInstance) {
            mInstance = new BaseApplication();
        }
        return mInstance;

    }
}
