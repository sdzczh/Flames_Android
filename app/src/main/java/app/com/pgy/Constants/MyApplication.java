package app.com.pgy.Constants;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.alibaba.security.rp.RPSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.HashMap;
import java.util.Map;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;

import static com.alibaba.security.rp.RPSDK.RPSDKEnv.RPSDKEnv_ONLINE;
import static app.com.pgy.Constants.Constants.DEBUG;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:
 *
 * @author 徐庆重
 */
public class MyApplication extends MultiDexApplication {
    private static MyApplication myApplication = null;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        Preferences.init(this);
        /*友盟debug模式，上线时关闭*/
//        Config.DEBUG = DEBUG;
//        UMShareAPI.get(this);
        UMConfigure.init(this,"5a614588b27b0a4cdd0003b8"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        //友盟统计，禁止默认的页面统计方式，这样将不会再自动统计Activity。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
        /*String deviceInfo = Utils.getDeviceInfo(this);
        LogUtils.w("设备信息",deviceInfo);*/
        /*阿里无线认证*/
        RPSDK.initialize(RPSDKEnv_ONLINE,this); //其中env始终为RPSDKEnv_ONLINE
        /*Bugly集成,第三个参数建议在测试阶段建议设置成true，发布时设置为false。*/
        CrashReport.initCrashReport(getApplicationContext(), "e87a6bb7d2", !DEBUG);
        /*融云IM集成*/
//        initRongyun();
        /*根据本地配置文件版本，去服务器获取最新配置文件*/
        getConfiguration(0);
    }


    {
        /**友盟分享各个平台的配置，建议放在全局Application或者程序入口
         * wx8ee94d3c8d7d9a58:8e8ce0cbc43429b50e041b96d2fc6068
         * 1106690432:7kwwDPldqWzgqu9m
         *
         * 1106805242：kdG9Al02koh2tHhy
         * */
        PlatformConfig.setWeixin("wx8d7486d6ff89998b","edd55e87062b379010cf193b57d3804d");
//        PlatformConfig.setQQZone("1106805242","kdG9Al02koh2tHhy");
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    /**从服务器获取配置文件*/
    private void getConfiguration(int currentConfigVersionCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("versionCode",currentConfigVersionCode);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType",SYSTEMTYPE_ANDROID);
        NetWorks.getConfig(map, new getBeanCallback<Configuration>() {
            @Override
            public void onSuccess(Configuration configuration) {
                /*将最新配置文件存储到本地*/
                boolean isHasConfig = Preferences.setConfiguration(configuration);
                Log.w("app",isHasConfig?"成功加载配置文件":"加载配置文件失败");
                LogUtils.w("app",configuration.toString());
            }

            @Override
            public void onError(int errorCode, String reason) {
                LogUtils.w("app","配置文件获取错误，重新获取:"+errorCode+",reason:"+reason);
                //getConfiguration(currentConfigVersionCode);
            }
        });
    }


    /**获取当前进程*/
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
