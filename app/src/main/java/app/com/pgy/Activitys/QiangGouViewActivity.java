package app.com.pgy.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import app.com.pgy.Activitys.Base.PermissionActivity;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Interfaces.getStringCallback;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.CalanderUtils;
import app.com.pgy.Utils.LoginUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Widgets.ExitDialog;
import app.com.pgy.Widgets.TitleView;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * *  @Description:描述
 * *  @Author: EDZ
 * *  @CreateDate: 2019/7/23 16:08
 */
public class QiangGouViewActivity extends PermissionActivity {
    /* 标题
     */
    @BindView(R.id.js_title)
    TitleView titleView;
    @BindView(R.id.wv_info)
    WebView wv_info;
    private boolean firtLoad = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_js_webview;
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        titleView.setTitle("PGY认购");
        titleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wv_info.canGoBack()){
                    wv_info.goBack();
                }else {
                    finish();
                }
            }
        });
        wv_info.addJavascriptInterface(QiangGouViewActivity.this, "android");
//        setWebViewAndUrl("http://47.104.142.76:8081/orderapi/web/activity.action",wv_info);
        initWebView();
    }


    private void initWebView() {

        WebSettings webSettings = wv_info.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv_info.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (firtLoad){
//                    getDkYue();
//                    firtLoad = false;
//                }

            }
        });
        wv_info.loadUrl("http://47.104.142.76:8081/orderapi/web/activity.action");
//        LogUtils.e("网址","世界杯竞猜=="+ Preferences.getConfiguration().getActivityUrl());
//        if (!TextUtils.isEmpty(Preferences.getConfiguration().getActivityUrl())){
//            wv_info.loadUrl(Preferences.getConfiguration().getActivityUrl());
//        }

    }


    //由于安全原因 targetSdkVersion>=17需要加 @JavascriptInterface
    //JS调用java，且无参
    @JavascriptInterface
    public void saveCalenderEvent(String hours) {

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        final int hour = Integer.parseInt(hours);
        final String title = "抢购提醒";
        final String msg = "将在下期抢购提前15分钟开启抢购提醒\n提醒时间:"+TimeUtils.getCurrentDate()+" "+(hour-1)+":45";
        final ExitDialog.Builder builder = new ExitDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setAndroid_msg(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("开启提醒",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Utils.IntentUtils(mContext,SecurityCenterActivity.class);
                        dialog.dismiss();
                        addCalandar(mContext,title,msg,hour);
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //JS调用java，且传参
    @JavascriptInterface
    public void buyAction(final String amount, final String ecnAmount) {
//        ToastUtils.ShortToast(this,"立即购买："+amount+","+ecnAmount);
        if (LoginUtils.isLogin(mContext)) {
            showPayDialog(new getStringCallback() {
                @Override
                public void getString(String string) {
                    buyOdin(amount,ecnAmount,string);
                }
            });
        }

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    private void buyOdin(String amount,String ecnAmount,String tradePw){
        showLoading(null);
        Map<String, Object> map = new HashMap<>();
        map.put("amount",amount);
        map.put("ecnAmount",ecnAmount);
        map.put("password",tradePw);
        map.put("deviceNum", Preferences.getDeviceId());
        map.put("systemType", SYSTEMTYPE_ANDROID);
        map.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.buyOdin(Preferences.getAccessToken(), map, new getBeanCallback() {
            @Override
            public void onSuccess(Object o) {
                hideLoading();
                showToast("认购成功");
            }

            @Override
            public void onError(int errorCode, String reason) {
                hideLoading();
                onFail(errorCode,reason);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (wv_info.canGoBack()){
            wv_info.goBack();
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void addCalandar(Context context, final String title, final String message, final int hour){
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                CalanderUtils.addCalanderEvent(mContext,title,message,hour);
            }
        },R.string.calander,Manifest.permission.WRITE_CALENDAR,Manifest.permission.READ_CALENDAR);
    }

}
