package app.com.pgy.Utils;

import android.text.TextUtils;
import android.util.Log;

/**
 /**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述: 日志工具类
 *
 * @author 徐庆重
 */

public class LogUtils {
    private static boolean DEBUG = true;
    public static void v(String tag, String message) {
        if(DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if(DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if(DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        message = TextUtils.isEmpty(message) ? "" : message;
        if(DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if(DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if(DEBUG) {
            Log.e(tag, message, e);
        }
    }

    /**打印*/
    public static void print(String message){
        if (TextUtils.isEmpty(message)){
            return;
        }
        if (DEBUG){
            //Log.w("print",message+",Time:"+TimeUtils.timeStampInt());
        }
        /*将日志上传服务*/
        /*NetWorks.uploadLog(message, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onError(int errorCode, String reason) {

            }
        });*/
    }
}
