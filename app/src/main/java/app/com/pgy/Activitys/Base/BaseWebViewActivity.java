package app.com.pgy.Activitys.Base;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.com.pgy.Utils.LogUtils;

/**
 * 创建日期：2017/11/22 0022 on 上午 11:23
 * 描述:WebView基类
 *
 * @author 徐庆重
 */
public abstract class BaseWebViewActivity extends PermissionActivity{

    /**初始化WebView界面*/
    protected void setWebViewAndUrl(String baseUrl, WebView baseWebView) {
        if (baseWebView == null){
            return;
        }
        /*url为空时，加载本地错误页面*/
        if (TextUtils.isEmpty(baseUrl) || !baseUrl.startsWith("http")){
            LogUtils.w(TAG,"url不正确");
            baseWebView.loadUrl("file:///android_asset/error.html");
            return;
        }
        LogUtils.w(TAG,"正在打开的url："+baseUrl);
        /*取消webView横向的bar显示*/
        baseWebView.setHorizontalScrollBarEnabled(false);
        baseWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        WebSettings webSettings = baseWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        // 设置 缓存模式
        /*if (!checkNetworkState()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
        }*/
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 支持多窗口
        webSettings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        baseWebView.loadUrl(baseUrl);
        baseWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.w(TAG,"pageStart");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                /*监听webView加载，根据网络情况设定面板可见和可点击*/
                super.onPageFinished(view, url);
                LogUtils.w(TAG,"pageFinished");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtils.w(TAG,"pageError");
                /*加载错误时，加载本地错误页面*/
                view.loadUrl("file:///android_asset/error.html");
            }
        });
    }

}
