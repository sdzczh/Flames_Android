package app.com.pgy.Widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import app.com.pgy.R;

/**
 * @author xuqingzhong
 *         加载中环形进度条
 */

public class LoadingProgress extends ProgressDialog {
    private View view;
    public LoadingProgress(Context context) {
        super(context);
    }

    public LoadingProgress(Context context, int theme , View view) {
        super(context, theme);
        this.view = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
            /*设置进度条是否可以按退回键取消*/
        setCancelable(true);
        // 设置点击进度对话框外的区域对话框不消失
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.layout_loading);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {//开启
        if (!isShowing()) {
            super.show();
        }
        if (view != null && view.isEnabled()){
            view.setEnabled(false);
        }
    }

    @Override
    public void dismiss() {//关闭
        if (isShowing()) {
            super.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (view != null && !view.isEnabled()){
            view.setEnabled(true);
        }
    }
}

