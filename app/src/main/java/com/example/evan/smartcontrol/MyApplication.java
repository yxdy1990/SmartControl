package com.example.evan.smartcontrol;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


/**
 * Created by Evan on 2017/12/17.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        initThirdPartyComponents();
        Logger.d("*** My Application onCreate ***");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        Logger.e("!!!!!! My Application onTerminate !!!!!!");
    }

    public static Context getInstance() {
        return mContext;
    }

    private void initThirdPartyComponents() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(1)
                .tag("SmartCtrl")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }
}
