package app.com.pgy.Activitys;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Activitys.Base.WebDetailActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.Configuration;
import app.com.pgy.Models.Beans.EventBean.EventLoginState;
import app.com.pgy.Models.Beans.version;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Services.DownloadService;
import app.com.pgy.Services.MyWebSocket;
import app.com.pgy.Utils.GlideCacheUtil;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.PersonalItemView;
import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/7/7.
 *我的系统设置页
 */

public class SystemSettingActivity extends PermissionActivity {
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.piv_activity_system_huancun)
    PersonalItemView piv_huancun;
    @BindView(R.id.piv_activity_system_version)
    PersonalItemView piv_version;
    @BindView(R.id.piv_activity_system_feeDoc)
    PersonalItemView piv_feeDoc;
    @BindView(R.id.tv_activity_system_loginout)
    TextView tv_loginOut;

    private String feeHelpUrl;
    private ExitDialog exitDialog;

    private ExitDialog updateDialog;
    private boolean updateFlag = false;
    /**更新状态，0是提示一次更新，1是每次都提示更新，2是强制更新*/
    private int updateState;
    /**服务器返回的最新版本*/
    private int lastVersionCodeFromNet;
    private String content;
    private String apkUrl;

    private static final int CHANGE_CACHE_SIZE = 0;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_CACHE_SIZE:
                    String size = (String) msg.obj;
                    /**
                     * 设置缓存大小
                     */
                    piv_huancun.setRightTxt(size);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_system_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tv_title.setText("系统设置");
        if (isLogin()){
            tv_loginOut.setVisibility(View.VISIBLE);
        }else {
            tv_loginOut.setVisibility(View.GONE);
        }
        piv_version.setRightTxt("V"+Preferences.getVersionName());
    }

    @Override
    protected void initData() {
        Configuration configuration = getConfiguration();
        feeHelpUrl = configuration.getRateDocUrl();
        String cacheSize = GlideCacheUtil.getInstance().getCacheSize(this);
        Message msg = Message.obtain();
        msg.what = CHANGE_CACHE_SIZE;
        msg.obj = cacheSize;
        mHandler.sendMessage(msg);
    }

    @OnClick({R.id.iv_back, R.id.piv_activity_system_huancun, R.id.piv_activity_system_version, R.id.piv_activity_system_feeDoc,
            R.id.tv_activity_system_loginout})
    public void onViewClicked(View view) {
        Intent intent2Detail = null;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            /*清理缓存*/
            case R.id.piv_activity_system_huancun:
                GlideCacheUtil.getInstance().clearImageAllCache(this);
                String cacheSize = GlideCacheUtil.getInstance().getCacheSize(this);
                Message msg = Message.obtain();
                msg.what = CHANGE_CACHE_SIZE;
                msg.obj = cacheSize;
                mHandler.sendMessage(msg);
                break;
            /*费率文档*/
            case R.id.piv_activity_system_feeDoc:
                intent2Detail = new Intent(mContext, WebDetailActivity.class);
                intent2Detail.putExtra("title","费率文档");
                intent2Detail.putExtra("url", feeHelpUrl);
                break;
            case R.id.piv_activity_system_version:
                // 版本更新
                getUpdateVersionInfor( Preferences.getVersionCode());
                break;
            case R.id.tv_activity_system_loginout:
                if (!isLogin()) {
                    showToast(R.string.unlogin);
                    return;
                }
                ShowExitDialog();
                break;
            default:break;
        }
        if (intent2Detail == null) {
            return;
        }
        startActivity(intent2Detail);
    }

    /**
     * 退出
     */
    private void ShowExitDialog() {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("确定要退出当前账号？");
        builder.setCancelable(true);

        builder.setPositiveButton("确定退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Preferences.clearAllUserData()) {
                            showToast("退出登录成功");
                            tv_loginOut.setVisibility(View.GONE);
                            sendLogoutMessage();
                        }
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        exitDialog = builder.create();
        exitDialog.show();
    }

    /**
     * 发送退出登录广播，在baseFragment和baseActivity中接收
     */
    private void sendLogoutMessage() {
        LogUtils.w("receiver", "Setting发送退出通知");
        EventBus.getDefault().post(new EventLoginState(false));
        //BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.EXIT);
        MyWebSocket.getMyWebSocket().stopSocket();
        finish();
    }


    /**从服务器获取版本信息*/
    private void getUpdateVersionInfor(int currentVersionCode) {
        showLoading(piv_version);
        Map<String,Object> map = new HashMap<>();
        map.put("version",currentVersionCode);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType",SYSTEMTYPE_ANDROID);
        NetWorks.getLastVersion(map, new getBeanCallback<version>() {
            @Override
            public void onSuccess(version version) {
                hideLoading();
                /*获取服务器端最新的版本号、是否更新、更新模式、更新提示内容、apk下载地址*/
                lastVersionCodeFromNet = version.getUpdateVersion();
                updateFlag = version.getUpdateFlag();
                updateState = version.getUpdateType();
                content = version.getContent();
                apkUrl = version.getApkUrl();
                updateStrategy();
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode, reason);
            }
        });
    }

    /**更新策略*/
    private void updateStrategy(){
        /*无需更新版本*/
        if (!updateFlag){
            showToast("已经是最新版本");
            return;
        }
        switch (updateState){
            /*提示一次更新*/
            case 0:
                /*有新版本了,跳出提示更新本地最新跳过版本*/
                if (Preferences.getLastJumpVersion(lastVersionCodeFromNet)) {
                    showUpdateVersionDialog(false);
                    Preferences.setLastJumpVersion(lastVersionCodeFromNet);
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
                break;
        }
    }

    /**显示更新提示*/
    private void showUpdateVersionDialog(final boolean isForceUpdate) {
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle("发现新版本");
        builder.setMessage(content);
        builder.setCancelable(false);

        builder.setPositiveButton("立即更新",
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
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isForceUpdate) {
                    dialog.dismiss();
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

}
