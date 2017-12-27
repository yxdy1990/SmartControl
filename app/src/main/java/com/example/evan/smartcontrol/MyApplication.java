package com.example.evan.smartcontrol;

import android.app.Application;
import android.content.Context;

import com.haier.uhome.usdk.api.interfaces.IuSDKCallback;
import com.haier.uhome.usdk.api.uSDKErrorConst;
import com.haier.uhome.usdk.api.uSDKLogLevelConst;
import com.haier.uhome.usdk.api.uSDKManager;
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
        initDeviceControlEnv();
        Logger.d("****** My Application onCreate ******");
    }

    public static Context getInstance() {
        return mContext;
    }

    private void initThirdPartyComponents() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
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

    private void initDeviceControlEnv() {
        final uSDKManager usdkMgr = uSDKManager.getSingleInstance();
        usdkMgr.init(this);
        usdkMgr.enableFeatures(uSDKManager.SDK_FEATURE_DEFAULT);
        usdkMgr.initLog(uSDKLogLevelConst.USDK_LOG_DEBUG, false, new IuSDKCallback() {
            @Override
            public void onCallback(uSDKErrorConst uSDKErrorConst) {
                Logger.d("initLog IuSDKCallback: " + uSDKErrorConst);
            }
        });
        usdkMgr.startSDK(new IuSDKCallback() {
            @Override
            public void onCallback(uSDKErrorConst uSDKErrorConst) {
                if (uSDKErrorConst == uSDKErrorConst.RET_USDK_OK) {
                    Logger.d("###### uSDK Start Success ######");
                } else {
                    Logger.e("@@@@@@ uSDK Start Error @@@@@@");
                }
            }
        });
        Logger.d("uSDK(%s) Init Completely.", usdkMgr.getuSDKVersion());
    }
}
