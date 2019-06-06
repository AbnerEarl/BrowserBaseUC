package com.wust.browserbaseuc;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class CheckApkExist {

    private static String ucPkgName = "com.uc.browser";

    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /** 测试：uc 浏览器检测*/
    public static boolean checkUCBrowserExist(Context context){
        return checkApkExist(context, ucPkgName);
    }
}
