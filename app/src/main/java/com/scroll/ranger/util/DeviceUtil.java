package com.scroll.ranger.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.scroll.ranger.GlobalApplication;

import java.io.File;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 设备信息工具类
 */
public class DeviceUtil {

    //获取文字需要显示的宽度
    public static float getTextWidth(String text, float Size) {
        TextPaint tp = new TextPaint();
        tp.setTextSize(sp2px(Size));
        return tp.measureText(text);
    }

    //获取屏幕宽度
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕高度
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    //设置字体大小如20sp
    public static float applyDimension(int unit, float value) {
        return TypedValue.applyDimension(unit, value, GlobalApplication.globalContext.getResources().getDisplayMetrics());
    }

    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dp2px(float dpValue) {
        float scale = GlobalApplication.globalContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dp(float pxValue) {
        float scale = GlobalApplication.globalContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //将sp值转换为px值
    public static float sp2px(float spValue) {
        float fontScale = GlobalApplication.globalContext.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale;
    }

    //将px值转换为sp值
    public static float px2sp(float pxValue) {
        float fontScale = GlobalApplication.globalContext.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / fontScale;
    }

    //计算SampleSize
    public static int calculateInSampleSize(int srcWidth, int srcHeight, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (srcHeight > reqHeight || srcWidth > reqWidth) {
            float scaleW = (float) srcWidth / (float) reqWidth;
            float scaleH = (float) srcHeight / (float) reqHeight;

            float sample = scaleW > scaleH ? scaleW : scaleH;
            // 只能是2的次幂
            if (sample < 3)
                inSampleSize = (int) sample;
            else if (sample < 6.5)
                inSampleSize = 4;
            else if (sample < 8)
                inSampleSize = 8;
            else
                inSampleSize = (int) sample;
        }
        return inSampleSize;
    }


    public static int getNavigationBarHeight() {
        Resources resources = GlobalApplication.globalContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    //获取UUID
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //获取版本号
    public static String getVersionName() {
        try {
            PackageInfo pi = GlobalApplication.globalContext.getPackageManager().getPackageInfo(GlobalApplication.globalContext.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取设备型号
    public static String getMODEL() {
        try {
            String device_model = Build.MODEL; // 设备型号
            return device_model;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //设备的系统版本
    public static String getSystemRelease() {
        try {
            String version_release = Build.VERSION.RELEASE; // 设备的系统版本
            return version_release;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //设备的系统版本
    public static String getSystemSDKVersion() {
        try {
            String version_sdk = Build.VERSION.SDK; // 设备SDK版本
            return version_sdk;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    //验证GPS是否打开
    public static boolean isGPSOpen(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //打开GPS设置
    public static void openGPSConfig(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        ((Activity) mContext).startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
    }

    //强制帮用户打开GPS
    public static final boolean openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取屏幕分辨率
     */
    public static float getScreenDensity(Context mContext) {
// TODO Auto-generated method stub
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float density = displayMetrics.density; //得到密度
//        float width = displayMetrics.widthPixels;//得到宽度
//        float height = displayMetrics.heightPixels;//得到高度.
        return density;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public final static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    /**
     * 创建一个用于拍照图片输出路径的Uri (FileProvider)
     *
     * @param context
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, getFileProviderName(context), file);
    }


    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }


}
