package app.com.pgy.Activitys.Base;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import butterknife.BindView;
import app.com.pgy.R;
import app.com.pgy.Utils.LogUtils;
import app.com.pgy.Widgets.TitleView;

/**
 * @author xuqingzhong
 *         web详情界面
 */

public class WebDetailActivity extends BaseWebViewActivity {

    @BindView(R.id.activity_webDetail_title)
    TitleView activityWebDetailTitle;
    @BindView(R.id.activity_webDetail_webView)
    WebView activityWebDetailWebView;
    @BindView(R.id.activity_webDetail_progressBar)
    ProgressBar activityWebDetailProgressBar;
    private String title;
    private String url;

    @Override
    public int getContentViewId() {
        return R.layout.activity_base_webdetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        activityWebDetailTitle.setTitle(title);
        activityWebDetailTitle.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebDetailActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        LogUtils.w(TAG, url);
        /*设置url，父类去初始化*/
        setWebViewAndUrl(url, activityWebDetailWebView);
        activityWebDetailWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    if (activityWebDetailProgressBar != null){{
                        activityWebDetailProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                        hideLoading();
                    }}

                }
                else{
                    if (activityWebDetailProgressBar != null){{
                        showLoading(null);
                        activityWebDetailProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        activityWebDetailProgressBar.setProgress(newProgress);//设置进度值
                    }}
                }
            }
        });
    }

}
