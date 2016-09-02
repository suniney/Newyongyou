package com.example.yanxu.newyongyou.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Wangyingji on 15/6/16.
 */
public class VersionUtils {
    /**
     2  * 获取版本号
     3  * @return 当前应用的版本号
     4  */
    public static  String getVersion(Context context) {
            try {
                    PackageManager manager = context.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                    String version = info.versionName;
                     return version;
                 } catch (Exception e) {
                     e.printStackTrace();
                    return "1.0";
                }
         }
}
