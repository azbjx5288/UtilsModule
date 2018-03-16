package com.gcp.utilsmodule;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;
public class VersionUtils {

	/**
	 * 判断系统版本是否高于指定的某个版本号
	 * 
	 * @param 指定版本号
	 * @return true, if is aPI compatible
	 */
	public static boolean isAPICompatible(int requiresAPILevel) {
		return Build.VERSION.SDK_INT > requiresAPILevel ? true : false;
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return the system version
	 */
	public static String getSystemVersion() {
		String osVersion = Build.VERSION.RELEASE;
		if (!TextUtils.isEmpty(osVersion)) {
			return osVersion;
		}
		return "此设备无版本信息";

	}

	/**
	 * 获取app版本号
	 * @param context 代表应用持有的上下文
	 * @return 返回app当前版本号
	 * @throws NameNotFoundException  context获取包名失败
	 */
	public static int getApplicationVersion(Context context) throws NameNotFoundException{
		return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		
	}
}
