package huoli.com.pgy.Activitys;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

import huoli.com.pgy.Activitys.Base.PermissionActivity;
import huoli.com.pgy.Constants.Preferences;
import huoli.com.pgy.Interfaces.getBeanCallback;
import huoli.com.pgy.Models.Beans.version;
import huoli.com.pgy.NetUtils.NetWorks;
import huoli.com.pgy.R;
import huoli.com.pgy.Services.DownloadService;
import huoli.com.pgy.Services.HeartbeatService;
import huoli.com.pgy.Utils.LogUtils;
import huoli.com.pgy.Utils.TimeUtils;
import huoli.com.pgy.Utils.Utils;
import huoli.com.pgy.Widgets.ExitDialog;

import static huoli.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Splash界面
 * @author xuqingzhong
 */

public class SplashActivity extends PermissionActivity {
    private ExitDialog updateDialog;
    private boolean updateFlag = false;
    /**更新状态，0是提示一次更新，1是每次都提示更新，2是强制更新*/
    private int updateState;
    /**服务器返回的最新版本*/
    private int lastVersionCodeFromNet;
    private String content;
    private String apkUrl;
    /**在splash页面停留的时间*/
    private static final int DELAY_TIME = 1000;

    @Override
    public int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        /*String cacheToken = Preferences.getTalkToken();
        if (!TextUtils.isEmpty(cacheToken)){
            RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
        }*/
        /*开启心跳*/
        startHeartBeatService();
        /*去服务器获取是否更新信息*/
        getUpdateVersionInfor(Preferences.getVersionCode());
    }

    /**开启心跳服务*/
    private void startHeartBeatService(){
        try {
            Intent serviceIntent = new Intent(mContext, HeartbeatService.class);
            startService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    /**跳转*/
    private void intent2Main() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SplashActivity.this.isDestroyed()){
                    return;
                }
                if (Preferences.isShowWelcome()){
                    Utils.IntentUtils(mContext,GuideActivity.class);
                }else {
                    Utils.IntentUtils(mContext,MainActivity.class);
                }
                SplashActivity.this.finish();
            }
        }, DELAY_TIME);
    }

    /**从服务器获取版本信息*/
    private void getUpdateVersionInfor(int currentVersionCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("version",currentVersionCode);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType",SYSTEMTYPE_ANDROID);
        NetWorks.getLastVersion(map, new getBeanCallback<version>() {
            @Override
            public void onSuccess(version version) {
                /*获取服务器端最新的版本号、是否更新、更新模式、更新提示内容、apk下载地址*/
//                version.setApkUrl("////");
//                version.setUpdateVersion(2);
//                version.setUpdateFlag(true);
//                version.setUpdateType(0);
//                version.setContent("test 测试");
                lastVersionCodeFromNet = version.getUpdateVersion();
                updateFlag = version.getUpdateFlag();
                updateState = version.getUpdateType();
                content = version.getContent();
                apkUrl = version.getApkUrl();
                updateStrategy();
            }

            @Override
            public void onError(int errorCode, String reason) {
                onFail(errorCode, reason);
                intent2Main();
            }
        });
    }

    /**更新策略*/
    private void updateStrategy(){
        /*无需更新版本*/
        if (!updateFlag){
            intent2Main();
            return;
        }
        switch (updateState){
            /*提示一次更新*/
            case 0:
                /*有新版本了,跳出提示更新本地最新跳过版本*/
                if (Preferences.getLastJumpVersion(lastVersionCodeFromNet)) {
                    showUpdateVersionDialog(false);
                    Preferences.setLastJumpVersion(lastVersionCodeFromNet);
                }else{
                    intent2Main();
                }
                break;
            /*每次都提示更新*/
            case 1:
                showUpdateVersionDialog(false);
                break;
            /*强制更新*/
            case 2:
                showUpdateVersionDialog(true);
                break;
            /*返回值错误*/
            default:
                intent2Main();
                break;
        }
    }

    /**显示更新提示*/
    private void showUpdateVersionDialog(final boolean isForceUpdate) {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("更新提示");
        builder.setMessage(content);
        builder.setCancelable(false);

        builder.setPositiveButton("更新",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                         /*请求读写权限*/
                        checkPermission(new CheckPermListener() {
                            @Override
                            public void superPermission() {
                                LogUtils.w("permission","SplashActivity:读写权限已经获取");
                                //downloadAPK(apkUrl,getResources().getString(R.string.app_name)+".apk");
                                if (TextUtils.isEmpty(apkUrl)){
                                    return;
                                }
                                startDownLoadService(apkUrl);
                            }
                        }, R.string.storage, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isForceUpdate) {
                    dialog.dismiss();
                    intent2Main();
                }
            }
        });
        updateDialog = builder.create();
        updateDialog.show();
    }

    /**开始下载服务*/
    private void startDownLoadService(String apkUrl) {
        showLoading(null);
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, apkUrl);
        isBindService = bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private boolean isBindService;
    /**下载服务回调*/
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
            DownloadService downloadService = binder.getService();
            //接口回调，下载进度
            downloadService.setOnProgressListener(new DownloadService.OnProgressListener() {
                @Override
                public void onProgress(float fraction) {
                    LogUtils.w(TAG, "下载进度：" + fraction);
                    //判断是否真的下载完成进行安装了，以及是否注册绑定过服务
                    if (fraction == DownloadService.UNBIND_SERVICE && isBindService) {
                        unbindService(conn);
                        isBindService = false;
                        showToast("下载完成！");
                        hideLoading();
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            hideLoading();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        /*每次取消安装后，再弹出更新提示*/
        if (updateDialog!= null && !updateDialog.isShowing()) {
            updateDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (updateDialog != null && updateDialog.isShowing()){
            updateDialog.dismiss();
        }
        hideLoading();
        super.onDestroy();
    }
}
