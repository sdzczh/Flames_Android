package app.com.pgy.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import app.com.pgy.Constants.Preferences;
import app.com.pgy.Constants.StaticDatas;
import app.com.pgy.Interfaces.getBeanCallback;
import app.com.pgy.Models.Beans.CoinAvailbalance;
import app.com.pgy.NetUtils.NetWorks;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Utils.TimeUtils;
import app.com.pgy.Utils.ToolsUtils;
import app.com.pgy.Widgets.TitleView;

import static app.com.pgy.Constants.StaticDatas.SYSTEMTYPE_ANDROID;

/**
 * Created by YX on 2018/6/14.
 */

public class ZhuanPanViewActivity extends Activity {
    /* 标题
     */
    @BindView(R.id.js_title)
    TitleView titleView;
    @BindView(R.id.wv_info)
    WebView wv_info;
    private Unbinder unbinder;
    private boolean firtLoad = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_webview);
        unbinder = ButterKnife.bind(this);
        initView();
        initWebView();
    }


    private void initView() {
        titleView.setTitle("COIN转盘");
        titleView.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhuanPanViewActivity.this.finish();
            }
        });
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
                if (firtLoad){
                    getDkYue();
                    firtLoad = false;
                }

            }
        });
//        wv_info.loadUrl("http://turn.yb.link");
        LogUtils.e("网址","世界杯竞猜=="+ Preferences.getConfiguration().getActivityUrl());
        if (!TextUtils.isEmpty(Preferences.getConfiguration().getActivityUrl())){
            wv_info.loadUrl(Preferences.getConfiguration().getActivityUrl());
        }
    }

    private void  getDkYue(){
        if (!Preferences.isLogin()){
            wv_info.post(new Runnable() {
                @Override
                public void run() {
                    wv_info.loadUrl("javascript:userLogout()");
                    wv_info.loadUrl(Preferences.getConfiguration().getActivityUrl());
                }
            });
            return;
        }
        Map<String,Object> maps = new HashMap<>();
        maps.put("accountType", StaticDatas.ACCOUNT_GOODS);
        maps.put("coinType", 8);
        maps.put("deviceNum", Preferences.getDeviceId());
        maps.put("systemType", SYSTEMTYPE_ANDROID);
        maps.put("timeStamp", TimeUtils.getUpLoadTime());
        NetWorks.getCoinAvailbalance(Preferences.getAccessToken(), maps, new getBeanCallback<CoinAvailbalance>() {
            @Override
            public void onSuccess(CoinAvailbalance coinAvailbalance) {
                if (coinAvailbalance != null){
                    sendJs(coinAvailbalance.getAvailBalance());
                }else {
                    Toast.makeText(ZhuanPanViewActivity.this,"获取数据为空，请稍候重试",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int errorCode, String reason) {
                Toast.makeText(ZhuanPanViewActivity.this,errorCode+":"+reason,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendJs(String dkAmount){
        Map<String,Object> maps = new HashMap<>();
        maps.put("key", Preferences.getLocalKey());
        maps.put("token", Preferences.getAccessToken());
        maps.put("phone", Preferences.getLocalUser().getPhone());
        maps.put("dkAmount",dkAmount);
        final String data = ToolsUtils.getAESParams(maps,"xpLbp7JdqU49LJuz");

        wv_info.post(new Runnable() {
            @Override
            public void run() {
                wv_info.loadUrl("javascript:userLogin('" + data + "')");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
