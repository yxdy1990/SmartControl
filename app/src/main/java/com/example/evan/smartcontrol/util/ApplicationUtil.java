package com.example.evan.smartcontrol.util;

import com.haier.uhome.usdk.api.uSDKDevice;
import com.haier.uhome.usdk.api.uSDKDeviceStatusConst;

/**
 * Created by Evan on 2017/12/17.
 */

public class ApplicationUtil {

    public static String getuSDKDeviceStatus(uSDKDevice devObj) {
        uSDKDeviceStatusConst status = devObj.getStatus();

        if(status == uSDKDeviceStatusConst.STATUS_CONNECTING) {
            return "连接中";
        } else if (status == uSDKDeviceStatusConst.STATUS_READY) {
            return "就绪";
        } else if (status == uSDKDeviceStatusConst.STATUS_OFFLINE) {
            return "离线";
        } if (status == uSDKDeviceStatusConst.STATUS_CONNECTED) {
            return "已连接";
        } else {
            return "未连接";
        }
    }

    public static String getuSDKDeviceType(uSDKDevice devObj) {
        if (AppConstant.DEV_LIGHT.equals(devObj.getUplusId()) ||
                AppConstant.DEV_ZHA_LIGHT.equals(devObj.getUplusId())) {
            return "灯光";
        } else if (AppConstant.DEV_CURATIN.equals(devObj.getUplusId()) ||
                AppConstant.DEV_ZHA_CURTAIN.equals(devObj.getUplusId())) {
            return "窗帘";
        } else if (AppConstant.DEV_YODAR.equals(devObj.getUplusId())) {
            return "悠达背景音乐";
        } else {
            return "未知设备";
        }
    }
}
